package com.aaroom.restcontroller;

import com.aaroom.beans.*;
import com.aaroom.exception.RestError;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.*;
import com.aaroom.utils.Constants;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import com.aaroom.wechat.service.MemberCommentService;
import com.shangmei.persistence.ShopBaseSMMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


@RestController
public class ShopController {

    @Autowired
    private ShopBaseService shopBaseService;

    @Autowired
    private HotelBaseService hotelBaseService;

    @Autowired
    private ShangmeiOTAService shangmeiOTAService;

    @Autowired
    private MemberCommentService memberCommentService;

    @Autowired
    private StorageService storageService;


    //增加或者修改酒店信息
    @PostMapping("/console/api/insertOrUpdateShopBase")
    public Object insertOrUpdateShopBase(@RequestBody HotelView hotelView, HttpServletRequest request)throws IOException {
        //数据写入事务
        shopBaseService.insertOrUpdateHotelView(hotelView);
        //同步前端js数据（id，hotel_name）
        shopBaseService.refreshHotels(request);
        //数据回显
        return new RestError(shopBaseService.getCurrentDataByHotelId(hotelView.getShopBase().getSmhotel_code()));
    }

//    @PostMapping("/abc")
    public Object abc(){
       // List<com.shangmei.beans.ShopBase> list = shopBaseSMMapper.getShopBaseSMByAABrandId();
        shopBaseService.updateChargeConfigPrice();
        return RestError.E_OK;

    }
    //查看酒店详情-酒店信息
    @PostMapping("/console/api/getShopBaseDetailByShopId")
    public Object getShopBaseDetailByShopId(@RequestParam(value = "hotel_id")String hotel_id){
        return shopBaseService.getCurrentDataByHotelId(hotel_id);
    }

    //获取酒店基础信息
    @PostMapping("/console/api/getShopBaseByHotelId")
    public Object getShopBaseByHotelId(@RequestParam(value = "begin_time",required = false)String begin_time,
                                       @RequestParam(value = "end_time",required = false)String end_time,
                                       @RequestParam(value = "province",required = false)String province,
                                       @RequestParam(value = "city",required = false)String city,
                                       @RequestParam(value = "hotel_bd",required = false)String hotel_bd,
                                       @RequestParam(value = "pageIndex",defaultValue = "1",required = false)Integer pageIndex,
                                       @RequestParam(value = "pageSize",defaultValue = "20",required = false)Integer pageSize) {
        Map params = new HashMap();
        params.put("begin_time",begin_time);
        params.put("end_time",end_time);
        params.put("province",province);
        params.put("city",city);
        params.put("hotel_bd",hotel_bd);
        List list = shopBaseService.selectShopBaseByParams(params);
        Page pageList = PageUtils.getPageList(list, pageIndex, pageSize);
        return  pageList;
    }

    //关闭酒店  (isActive=0)
   // @Scheduled(cron = "0 0 0 * * ?")
   // @PostMapping("/console/api/closeShopByHotelId")
    public Object closeShopByHotelId(@RequestParam(value = "id")Integer id,
                                     @RequestParam(value = "is_active")Byte is_active) {
        ShopBase shopBase = shopBaseService.selectByPrimaryKey(id);
        if (shopBase == null){
            return RestError.E_DATA_INVALID;
        }
        shopBase.setIs_active(is_active);
        shopBaseService.updateByPrimaryKeySelective(shopBase);
        return RestError.E_OK;
    }

    //调用尚美ota接口，根据酒店id获取基础信息同步至aa数据库
//    @PostMapping("/Test/console/api/getHotelId")
    public Object getHotelId(){
        Map params = new HashMap();
        params.put("brand_id","ffda849b054a11e6404d00163e006eee");
        List<HotelBase> listByMap = hotelBaseService.getListByParams(params);

        for (HotelBase hotelBase:listByMap){
            String smhotel_id = hotelBase.getSmhotel_id();
            ShangmeiRetView exchange =shangmeiOTAService.exchange(Constants.HOTEL_INFO +smhotel_id, HttpMethod.POST, null, ShangmeiRetView.class);
            Map<String,Object> entity = (Map<String,Object>)exchange.getEntity();
            String hotel_code = (String)entity.get("hotel_code");
            String fax = (String)entity.get("fax");
            String status = (String)entity.get("status"); //运营状态
            String hotel_name = (String)entity.get("hotel_name");
            String city = (String)entity.get("city");//市
            String area = (String)entity.get("area"); //AA ROOM
            String county = (String)entity.get("county"); //区
            String province = (String)entity.get("province"); //省
            String address = (String)entity.get("address"); //详细地址
            String telephone = (String)entity.get("telephone"); //详细地址


            //插入到shopbase，有则传，无则传null
            ShopBase sb = new ShopBase();
            sb.setAddress_detail(address);
            sb.setSmhotel_code(hotel_code);
            sb.setShop_status(status);
            sb.setShop_name(hotel_name);
            sb.setCity(city);
            sb.setProvince(province);
            sb.setArea(county);
            sb.setHotel_brand(area);
            shopBaseService.insertSelective(sb);
        }
        return RestError.E_OK;
    }

    //查询酒店列表信息
    @PostMapping(value = "/console/api/getHotelListOnComment")
    public Object getHotelListOnComment(){
        List<Map<String,Object>> commentListByParams = memberCommentService.getCommentListByParams(new HashMap());
        if (commentListByParams.size()>0) {
            return commentListByParams;
        }
        return RestError.E_DATA_FAIL;
    }

    //添加或者修改文件
    @PostMapping(value = "/console/api/insertOrUpdateWeChatPhotos")
    public Object insertOrUpdateWeChatPhotos(@RequestParam(value = "file") MultipartFile file,
                                             @RequestParam(value = "hotel_id") String hotel_id,
                                             @RequestParam(value = "pic_type") String pic_type,
                                             @RequestParam(value = "comment") String comment,
                                             @RequestParam(value = "id",required = false) Integer id,
                                             @RequestParam(value = "url",required = false) String url) {
        if (id==null){//新增
            if (file != null ) {
                String destFileName = shopBaseService.uploadFile(file);
                Integer activePic = shopBaseService.getActivePic(hotel_id, pic_type, destFileName, comment);
                if (activePic!=null){
                    //上传成功，则is_active 0->1
                    Map map = new HashMap();
                    map.put("is_active",1);
                    map.put("id",activePic);
                    shopBaseService.updateByPrimaryKeySelectivePic(map);
                    Map map1 = new HashMap();
                    map1.put("id",activePic);
                    return new RestError(shopBaseService.selectByShopPicturePrimaryKey(map1).get(0));//返回上传的图片信息
                }
                return RestError.E_DATA_INVALID;
            }
            return RestError.E_DATA_INVALID;
        }else {//修改
            String destFileName = shopBaseService.uploadFile(file);
            Map map = new HashMap();
            map.put("id",id);
            map.put("url",destFileName);
            map.put("is_active",1);
            //oss删除文件
            storageService.deleteFile(url);
            //更新操作
            shopBaseService.updateByPrimaryKeySelectivePic(map);
            Map map1 = new HashMap();
            map1.put("id",id);
            return new RestError(shopBaseService.selectByShopPicturePrimaryKey(map1).get(0));
        }
    }


    //文件展示
    @PostMapping(value = "/console/api/showWeChatPhotos")
    public Object showWeChatPhotos(@RequestParam(value = "hotel_id") String hotel_id) {
        Map<String,Object> finalMap = new LinkedHashMap();

        Map map_ = new HashMap();
        Map picMap ;

        map_.put("smhotel_code",hotel_id);
        map_.put("is_active",(byte)1);
        finalMap.put("shop_name",shopBaseService.selectByParams(map_).getShop_name());

        List coverList = new ArrayList();
        List withinList = new ArrayList();
        List modelList = new ArrayList();

        Map map = new HashMap();
        map.put("hotel_id",hotel_id);
        map.put("is_active",1);
        List<ShopPicture> shopPictures = shopBaseService.selectByShopPicturePrimaryKey(map);

        if (shopPictures.size()>0){
            for (ShopPicture sp:shopPictures){//根据type分类
                picMap = new HashMap();
                String s = storageService.generatePresignUrl(sp.getUrl());  //返回图片地址
                picMap.put("id",sp.getId());
                picMap.put("url",s);
                picMap.put("pic_name",sp.getUrl());
                picMap.put("comment",sp.getComment());
                if ("cover".equals(sp.getPic_type())){
                   coverList.add(picMap);
               }else if ("within".equals(sp.getPic_type())){
                   withinList.add(picMap);
               }else {
                   modelList.add(picMap);
               }
            }
            finalMap.put("coverList",coverList);
            finalMap.put("withinList",withinList);
            finalMap.put("modelList",modelList);
        }
        finalMap.put("coverList",coverList);
        finalMap.put("withinList",withinList);
        finalMap.put("modelList",modelList);
        return finalMap;
    }

    //文件改备注
    @PostMapping(value = "/console/api/updateWeChatPhotosCommentById")
    public Object updateWeChatPhotosCommentById(@RequestParam(value = "id") Integer id,
                                                @RequestParam(value = "comment") String comment) {
        Map map = new HashMap();
        map.put("id",id);
        map.put("comment",comment);
        shopBaseService.updateByPrimaryKeySelectivePic(map);
        return RestError.E_OK;
    }

    //根据酒店id获取房態，(目前只有00650酒店有数据其他为空)
    @PostMapping("/console/api/room_type_list")
    public Object room_type_list(@RequestParam(value = "hotel_id")String hotel_id){
        return shopBaseService.getHotelRoomTypeDetailByHotel(hotel_id);
    }

    //文件删除
    @PostMapping(value = "/console/api/deleteWeChatPhotosByIds")
    public Object deleteWeChatPhotosByIds(@RequestParam(value = "id") Integer id,
                                          @RequestParam(value = "pic_name") String pic_name) {
        //数据库删除文件
        shopBaseService.deleteByPrimaryKey(id);
        //oss删除文件
        storageService.deleteFile(pic_name);
        return RestError.E_OK;
    }
}
