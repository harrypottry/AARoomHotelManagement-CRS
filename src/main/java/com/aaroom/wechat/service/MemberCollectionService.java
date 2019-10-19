package com.aaroom.wechat.service;

import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.ShangmeiOTAService;
import com.aaroom.utils.Constants;
import com.aaroom.utils.TencentMapAPIUtils;
import com.aaroom.wechat.bean.MemberCollection;
import com.aaroom.wechat.bean.MemberComment;
import com.aaroom.wechat.persistence.MemberCollectionMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberCollectionService {

    @Resource
    private MemberCollectionMapper memberCollectionMapper;

    @Autowired
    private ShangmeiOTAService shangmeiOTAService;

    @Autowired
    private MemberCommentService memberCommentService;

    private static final String FROMADDRESS = "https://apis.map.qq.com/ws/distance/v1/?mode=driving&from=";

    private final static String TENCENTKEY ="HXXBZ-NM7WJ-FH3FA-KHY6Y-B4ONF-APFJ7";


    public List<MemberCollection> getMemberCollectionByMemberId(String member_id){
        return memberCollectionMapper.getMemberCollectionByMemberId(member_id);
    }

    public List<MemberCollection> getMemberCollectionByEntity(Map<String,String> params){
        return memberCollectionMapper.getMemberCollectionByEntity(params);
    }

    public int insert(MemberCollection record){
        return memberCollectionMapper.insertSelective(record);
    }

    public int deleteByEntity(MemberCollection record){
        return memberCollectionMapper.deleteByEntity(record);
    }

    public Object getHotelInfoByHotelId(List<String> hotel_ids,String lon,String lat){
        List<Map<String,Object>> result = new ArrayList<>();
        Map<String,Object> tempMap;
        for (String hotel_id:hotel_ids){
            //ota ，经纬度，最低房价，酒店名称，评分
            ShangmeiRetView exchange =shangmeiOTAService.exchange(Constants.HOTELURL+hotel_id, HttpMethod.POST, null, ShangmeiRetView.class);
            if (exchange.getError_code()!=1001){  //二次检验，出错机制

                Map<String,Object> entity = (Map<String,Object>)exchange.getEntity();

                tempMap = new HashMap<>();
                if (StringUtils.isNotBlank(lon) && StringUtils.isNotBlank(lat)){
                    double longitude = (double)entity.get("longitude");
                    double latitude = (double)entity.get("latitude");
                    String fromAddress = lat + ","+ lon;
                    String toAddress = latitude + "," + longitude;
                    //调取腾讯地图api
                    String urlString = FROMADDRESS+fromAddress+"&to="+toAddress+"&key="+TENCENTKEY;
                    String location = TencentMapAPIUtils.getLocation(urlString);
                    // 转JSON格式
                    JSONObject jsonObject = JSONObject.fromObject(location).getJSONObject("result");
                    //elements是[](数组格式)所以使用JSONArray获取
                    JSONArray adInfo = jsonObject.getJSONArray("elements");
                    //for数组
                    for (int j = 0; j < adInfo.size(); j++){
                        JSONObject jsonObject2 = adInfo.getJSONObject(j);
                        //获取距离(获取到的是m为单位)
                        String distance = jsonObject2.getString("distance");
                        double distanceD = Double.valueOf(distance);
                        //转化为km
                        distanceD = distanceD / 1000;
                        tempMap.put("distanceD",distanceD);//计算距离
                    }
                }
                //ota接口获取数据
                tempMap.put("hotel_id",hotel_id);
                tempMap.put("hotel_name",entity.get("hotel_name"));
                tempMap.put("address",entity.get("address"));
                //aa数据库获取酒店图片信息，评分信息，最低房价信息
                Map params = new HashMap();
                params.put("hotel_id", hotel_id);
                List<MemberComment> mcList = memberCommentService.getMemberCommentByParams(params);
                if(mcList.size()>0){
                    //计算平均评分
                    int sum =0;
                    for (MemberComment mc:mcList){
                        String utility = mc.getUtility();
                        try {
                            int a = Integer.parseInt(utility);
                            sum+=a;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    tempMap.put("hotel_star",sum/mcList.size());
                }
                //查询图片信息,完善中台系统后进行补充
                //查询最低房价  (暂无ota接口可以获取)
                result.add(tempMap);
            }
        }
        return result;
    }

    public double getHotelInfo(String lon,String lat,String hotel_id){
        double distanceD =0.00;
        ShangmeiRetView exchange =shangmeiOTAService.exchange(Constants.HOTELURL+hotel_id, HttpMethod.POST, null, ShangmeiRetView.class);
        if (exchange.getError_code()!=1008) {
            Map<String,Object> entity = (Map<String,Object>)exchange.getEntity();

            if (StringUtils.isNotBlank(lon)
                    && StringUtils.isNotBlank(lat)
                    && !"0.0".equals(entity.get("latitude"))
                    && !"0.0".equals(entity.get("longitude")) ){
                String fromAddress = lat + ","+ lon;
                String toAddress = entity.get("latitude") + "," + entity.get("longitude");
                //调取腾讯地图api

                String urlString = FROMADDRESS+fromAddress+"&to="+toAddress+"&key="+TENCENTKEY;
                String location = TencentMapAPIUtils.getLocation(urlString);
                JSONObject jsonObject = JSONObject.fromObject(location).getJSONObject("result");

                for (int j = 0; j < jsonObject.getJSONArray("elements").size(); j++){
                    JSONObject jsonObject2 = jsonObject.getJSONArray("elements").getJSONObject(j);
                    //获取距离(获取到的是m为单位)
                    distanceD = Double.valueOf(jsonObject2.getString("distance"));
                    //转化为km
                    distanceD = distanceD / 1000;
                }
            }
        }//二次检验，出错机制
        return distanceD;
    }
}
