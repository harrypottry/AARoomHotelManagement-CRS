package com.aaroom.service;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.persistence.*;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Constants;
import com.aaroom.utils.PinyinUtil;
import com.alibaba.fastjson.JSON;
import com.shangmei.persistence.ShopBaseSMMapper;
import com.shangmei.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class ShopBaseService {

    @Resource
    private ShopBaseMapper shopBaseMapper;

    @Resource
    private ShopContactsMapper shopContactsMapper;

    @Resource
    private ShopTrafficMapper shopTrafficMapper;

    @Resource
    private ShopServiceMapper shopServiceMapper;

    @Resource
    private ShopPictureMapper shopPictureMapper;

    @Resource
    private ShangmeiOTAService shangmeiOTAService;

    @Resource
    private StorageService storageService;

    @Resource
    private ShopBaseSMMapper shopBaseSMMapper;

    //定时执行酒店基础信息
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateChargeConfigPrice() {
        List<com.shangmei.beans.ShopBase> hotel_sm_list = shopBaseSMMapper.getShopBaseSMByAABrandId();
        List<String> sm_list = new ArrayList<>();
        List<String> aa_list = new ArrayList<>();
        for(com.shangmei.beans.ShopBase sm_sb:hotel_sm_list){
            String sm_shop_name = sm_sb.getShopname();
            sm_list.add(sm_sb.getShopid());
           for (ShopBase sb:shopBaseMapper.selectShopBaseByParams(new HashMap<>())){
               String smhotel_code = sb.getSmhotel_code();
               if (smhotel_code.equals(sm_sb.getShopid())){
                   //+
                   Map map = new HashMap();
                   map.put("smhotel_code",smhotel_code);
                   map.put("is_active",(byte)1);
                   ShopBase shopBase = shopBaseMapper.selectByParams(map);
                   shopBase.setShop_name(sm_shop_name);
                   shopBaseMapper.updateByPrimaryKeySelective(shopBase);
                   continue;
               }
           }
        }
        for (ShopBase sb:shopBaseMapper.selectShopBaseByParams(new HashMap<>())){
            aa_list.add(sb.getSmhotel_code());
        }
        sm_list.removeAll(aa_list);
        if (sm_list.size()>0){
            //insert
            for (String str:sm_list){
                com.shangmei.beans.ShopBase dataByHotelId = shopBaseSMMapper.getDataByHotelId(str);
                shopBaseMapper.insertSelective(new ShopBase(dataByHotelId.getShopname(),dataByHotelId.getShopid()));
            }
        }
    }

    public String uploadFile(MultipartFile file){

        String fileName = file.getOriginalFilename();
        Integer spitPoint = fileName.lastIndexOf(".");

        File temp = null;
        try {
            temp = File.createTempFile(UUID.randomUUID().toString(), fileName.substring(spitPoint));
            file.transferTo(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String destFileName = UUID.randomUUID().toString()+fileName.substring(spitPoint);
        storageService.uploadFile(temp, destFileName);  //向oss文件上传
        return destFileName;
    }

    public int insertSelective(ShopBase record){
        return shopBaseMapper.insertSelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getActivePic(String hotel_id,String pic_type,String destFileName,String comment){
        //持久化到数据库
        this.insertSelective(new ShopPicture(hotel_id,pic_type,destFileName,(byte)0,comment));
        //返回图片id
        Map map = new HashMap();
        map.put("is_active",0);
        List<ShopPicture> shopPictures = this.selectByShopPicturePrimaryKey(map);
        return shopPictures.get(0).getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateHotelView(HotelView record){
        //判断是否已经存在这个酒店
        Map params = new HashMap();
        List<ShopBase> allShopBase = shopBaseMapper.selectShopBaseByParams(params);
        String tempId = null;
        for (ShopBase sb:allShopBase){
            if (sb.getSmhotel_code() .equals(record.getShopBase().getSmhotel_code())  && sb.getIs_active() == 1){
                tempId = sb.getSmhotel_code();
                break;
            }
        }
        if (tempId!=null){  //update与insert操作
            //1=1酒店基本信息
            //list数组
            //将数组转换为字符串
            record.getShopBase().setChannel(getStrByList(record.getChannel()));
            record.getShopBase().setPay_method(getStrByList(record.getPay_method()));
            record.getShopBase().setShop_service(getStrByList(record.getShop_service()));
            if (record.getShopBase().getId() == null){
                this.insertSelective(record.getShopBase());
            }else {
                this.shopBaseMapper.updateByPrimaryKeySelective(record.getShopBase());  //这里的实体必须要传id
            }
            //1=n酒店额外信息
            for (ShopContacts sc:record.getShopContactsList()){
                if (sc.getId() == null){
                    shopContactsMapper.insertSelective(sc);
                }
                shopContactsMapper.updateByPrimaryKeySelective(sc); //这里的实体必须要传id
            }  //联系人信息  1=1 酒店信息
            shopServiceMapper.deleteByShopId(record.getShopBase().getSmhotel_code());  //现将数据清空
            for (ShopService ss:record.getShopWifiList()){  //遍历添加
                shopServiceMapper.insertSelective(ss);
            }//wifiList
            shopTrafficMapper.deleteByShopId(record.getShopBase().getSmhotel_code());
            for (ShopTraffic st:record.getShopTrafficList()){
                shopTrafficMapper.insertSelective(st);
            }//交通List
        }else {
            //插入酒店1=1基本信息
            record.getShopBase().setShop_service(getStrByList(record.getShop_service()));
            record.getShopBase().setChannel(getStrByList(record.getChannel()));
            record.getShopBase().setPay_method(getStrByList(record.getPay_method()));
            this.insertSelective(record.getShopBase());
            //插入酒店1=n基本信息(这里所有的list至少有一条数据，前端表单提交有拦截)
            for (ShopContacts sc:record.getShopContactsList()){
                shopContactsMapper.insertSelective(sc);
            }
            for (ShopService ss:record.getShopWifiList()){
                shopServiceMapper.insertSelective(ss);
            }
            for (ShopTraffic st:record.getShopTrafficList()){
                shopTrafficMapper.insertSelective(st);
            }
        }
    }

    public int updateByPrimaryKeySelective(ShopBase record){
        return shopBaseMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKeySelectivePic(Map<String,Object> map){
        return shopPictureMapper.updateByPrimaryKeySelective(map);
    }

    public ShopBase selectByPrimaryKey(Integer id){
        return shopBaseMapper.selectByPrimaryKey(id);
    }

    public ShopBase selectByParams(Map<String,Object> params){
        return shopBaseMapper.selectByParams(params);
    }

    public List<ShopBase> selectShopBaseByParams(Map<String,Object> params){
        return shopBaseMapper.selectShopBaseByParams(params);
    }

    public static String getStrByList(List<Integer> list){
        String str = new String();
        if (list.size()>0){
            for (Integer value:list){
                str += value+ ",";
            }
            str = str.substring(0,str.lastIndexOf(","));
        }
        return str;
    }

    public static List getListByStr(String str){
        List<Integer> list = new ArrayList();
        if (!StringUtils.isEmpty(str)){
            String[] split = str.split(",");
            for (String s:split){
                try {
                    int a = Integer.parseInt(s);
                    list.add(a);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    //更新 -酒店id-酒店名称 -io流写入到Js文件

    public Object refreshHotels(HttpServletRequest request) throws IOException {

        StringBuilder sb = new StringBuilder();

        sb.append("// 酒店列表\r\n");

        //组织数据

        List<ShopBase> shopBases = shopBaseMapper.selectShopBaseByParams(new HashMap());
        {
            List<List<String>> hotels = new ArrayList<>();
            PinyinUtil pinyinUtil = new PinyinUtil();
            for (ShopBase shopBase : shopBases) {
                List<String> dataMap = new ArrayList<>();
                dataMap.add(shopBase.getId().toString());
                dataMap.add(shopBase.getShop_name());
                dataMap.add(pinyinUtil.getUpperCase(shopBase.getShop_name(), true) + pinyinUtil.getUpperCase(shopBase.getShop_name(), false));
                hotels.add(dataMap);
            }
            sb.append("var hotels=");
            sb.append(JSON.toJSONString(hotels));
        }
        sb.append(";\r\n");

        {
            sb.append("var hotelbug=");
            sb.append("{");
            for (ShopBase shopBase : shopBases) {
                sb.append("\""+shopBase.getId().toString()+"\""+":"+"\""+shopBase.getShop_name()+"\""+",");
            }
            //sb.substring(0,sb.length()-3);
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append("};");

        FileWriter fileWriter = null;
        try {
            String filePath = request.getSession().getServletContext().getRealPath("/");
            File file = new File(filePath+"import/adminlte/js/options.js");
            boolean exists = file.exists();
            if(!exists) {
                boolean newFile = file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(sb.toString());
        }catch (IOException e) {
            e.printStackTrace();
            return RestError.E_DATA_FAIL;
        }finally {
            fileWriter.close();
        }

        return RestError.E_OK;
    }



    public int insertSelective(ShopPicture record){
        return shopPictureMapper.insertSelective(record);
    }

    public List<ShopPicture> selectByShopPicturePrimaryKey(Map map){
        return shopPictureMapper.selectByShopPicturePrimaryKey(map);
    }


    public Map<String,Object> getCurrentDataByHotelId(String smhotel_code){
        Map<String,Object> ret = new LinkedHashMap<>();
        Map params = new HashMap();
        params.put("smhotel_code",smhotel_code);
        params.put("is_active",(byte)1);
        ret.put("shopBase",shopBaseMapper.selectByParams(params));
        Map param = new HashMap();
        param.put("shop_id",smhotel_code);
        ret.put("shopContactsList",shopContactsMapper.getShopContactsList(param));
        ret.put("shopTrafficList",shopTrafficMapper.selectByParams(param));
        ret.put("shopWifiList",shopServiceMapper.selectByParams(param));
        ret.put("shop_service",getListByStr(shopBaseMapper.selectByParams(params).getShop_service()));
        ret.put("channel",getListByStr(shopBaseMapper.selectByParams(params).getChannel()));
        ret.put("pay_method",getListByStr(shopBaseMapper.selectByParams(params).getPay_method()));
        return  ret;
    }

    public Object getHotelRoomTypeDetailByHotel(String hotel_id){
        //data
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        //酒店-房型ota接口
        ShangmeiRetView exchange = shangmeiOTAService.exchange(Constants.HOTEL_ROOMTYPE+hotel_id, HttpMethod.POST, new HashMap<>(), ShangmeiRetView.class);
        if (exchange.getEntity()==null){
            return RestError.E_DATA_FAIL;
        }
        List<Map<String,String>> entity1 =(List<Map<String,String>>) exchange.getEntity();
        for (Map<String,String> map:entity1){
            String room_type_code =  map.get("room_type_code");  //房型代碼
            params.put("has_window",map.get("has_window"));  //
            params.put("room_type_name",map.get("room_type_name"));  //
            params.put("bed_type",map.get("bed_type"));  //
            params.put("guest_number",map.get("guest_number"));
            params.put("building_area",map.get("building_area"));
            params.put("floor_range",map.get("floor_range"));
            params.put("room_type_code",room_type_code);
            //房型-房價ota接口
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("hotel_code",hotel_id);
            paramMap.put("room_type_code",room_type_code);
            paramMap.put("start", DateUtil.format(new Date(),"yyyy-MM-dd"));
            paramMap.put("end",DateUtil.format(DateUtil.addDays(new Date(),15)));

            ShangmeiRetView exchange1 =shangmeiOTAService.exchange(Constants.AVAIL, HttpMethod.POST, paramMap, ShangmeiRetView.class);
            Map<String,Object> entity =(Map<String,Object>) exchange1.getEntity();
            List<Map<String,Object>> pricesList = (List<Map<String,Object>>)entity.get("prices");//此酒店所有房型集合
            for (Map<String,Object> MAP:pricesList){
                if (MAP.get("room_type_code").equals(room_type_code)){  //當前酒店，當前房型
                    List<Map<String,Object>> room_type_prices_list = (List<Map<String,Object>>)MAP.get("room_type_prices");//結果集
                    params.put("room_type_prices",room_type_prices_list);
                }
            }
            ret.add(params);
        }
        return ret;
    }

    public  int deleteByPrimaryKey(Integer id){
        return shopPictureMapper.deleteByPrimaryKey(id);
    }
}
