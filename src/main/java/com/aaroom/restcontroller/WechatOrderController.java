package com.aaroom.restcontroller;

import com.aaroom.model.OrderParam;
import com.aaroom.model.Price;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.HotelBaseService;
import com.aaroom.service.ShangmeiOTAService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.TencentMapAPIUtils;
import com.aaroom.wechat.controller.WxOrderController;
import com.aaroom.wechat.pay.MyWXpayConfig;
import com.aaroom.wechat.pay.WXPay;
import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by sosoda on 2019/2/20.
 * C端小程序后台接口命名规则：
 * /WeChat/console/api/+XXX
 */
@RestController
public class WechatOrderController {

    @Autowired
    private ShangmeiOTAService shangmeiOTAService;
    
    @Autowired
    private HotelBaseService hotelBaseService;

    @Autowired
    private WxOrderController wxOrderController;

    private final static String Provinces ="http://120.92.148.25:8099/rest/Geo/Provinces";

    private final static String cities ="http://120.92.148.25:8099/rest/Geo/Cities";

    private final static String TENCENTKEY ="HXXBZ-NM7WJ-FH3FA-KHY6Y-B4ONF-APFJ7";

    private final static String TENCENTMAP ="http://apis.map.qq.com/ws/geocoder/v1/?location=";

    private final static String AVAIL = "http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/avail"; //查询房态

//    @GetMapping("/Provinces")
    public Object Provinces(){
        return shangmeiOTAService.exchange(Provinces, HttpMethod.GET, null, ShangmeiRetView.class);
    }


//    @GetMapping("/cities")
    public Object cities(@RequestParam(value = "province_code",required = false) String province_code,
                         @RequestParam(value = "city_code",required = false)  String city_code){
        Map<String, String> params = new HashMap<>();
        params.put("province_code",province_code);
        params.put("city_code",city_code);
       return shangmeiOTAService.exchange(cities, HttpMethod.GET, params, ShangmeiRetView.class);
    }


//    @GetMapping("/hotels")
    public Object hotels(HttpServletRequest request){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page","2");
        params.add("per_page","20");
        ShangmeiRetView exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/static/hotel/list", HttpMethod.GET, params, ShangmeiRetView.class);
        return exchange;
    }
    //房态，查询近30天内的房量以及价格
//    @GetMapping("/hotel")
    public Object hotel(){
        Map<String, String> params = new HashMap<>();
        params.put("hotel_code","66667");
        params.put("start","2019-03-01");
        params.put("end","2019-03-02");
        String exchange = shangmeiOTAService.exchange(AVAIL, HttpMethod.POST, params, String.class);
        return exchange;
    }

   @PostMapping("/order")
    public Object order(HttpServletRequest request){
        OrderParam orderParam = new OrderParam();
        orderParam.setChannel_order_no(CommonUtil.createUUID());
        orderParam.setCustomer_name("bb");
        orderParam.setContact_name("aa");
        orderParam.setContact_phone("13512341234");
        orderParam.setHotel_code("00650");
        orderParam.setRoom_num(1);
        orderParam.setRoom_type_code("10000");
        orderParam.setStart("2019-03-30");
        orderParam.setEnd("2019-03-31");
        orderParam.setArrival("2019-03-31 21:00:00");
        orderParam.setPrice_total((double) 100);
        orderParam.setPay_status(1);
        orderParam.setHasInvoice(0);
        orderParam.setMemberId("1b0f461884bf4fdfb2d075c3190edb6f");
        orderParam.setPriceCode("Wprc");
        List<Price> list = new ArrayList();
        Price price = new Price();
        price.setStart("2019-03-31");
        price.setEnd("2019-03-31");
        price.setPrice(100);

        /*map.put("aa_plus_price","88");
        map.put("aa_pro_price","85");*/
        /*Map map2 = new HashMap();
        map.put("start","2019-03-20");
        map.put("end","2019-03-21");
        map.put("price","92");
        Map map3 = new HashMap();
        map.put("start","2019-03-21");
        map.put("end","2019-03-22");
        map.put("price","92");*/
        list.add(price);
        /*list.add(map2);
        list.add(map3);*/
        orderParam.setPrice_list(list);
        Object order = wxOrderController.order(orderParam);
        //ShangmeiRetView exchange = shangmeiOTAService.exchange("http://openapi.pms.ethank.com.cn/rest/PMSGateway/AAWECHAT/order/save", HttpMethod.POST, orderParam, ShangmeiRetView.class);
        return order;
    }

//    @PostMapping("/selectorder")
    public ShangmeiRetView selectorder(HttpServletRequest request){
        Map<String, String> params = new HashMap<>();
        params.put("order_no","JD01313160506003001");
        //params.put("room_type_code","012733");
      //  params.put("channel_order_no","2019-02-20");

        ShangmeiRetView exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/PMSGateway/AAWECHAT/order/query", HttpMethod.POST, params, ShangmeiRetView.class);
        return exchange;
    }


//    @PostMapping("/roomtype")
    public ShangmeiRetView roomtype(){
        Map<String, String> params = new HashMap<>();
        params.put("hotelcode","01273");
        params.put("roomtype_code","012731");

        ShangmeiRetView exchange = shangmeiOTAService.exchange("http://120.92.148.25:8099/rest/static/hotel/roomtype/:hotel_code/:room_type_code", HttpMethod.POST, JSON.toJSONString(params), ShangmeiRetView.class);
        return exchange;
    }


//    @PostMapping("/createOrder")
    public Object createOrder(HttpServletRequest request
                                ,@RequestBody Map<String, String> data) throws Exception {

        WXPay wxPay = new WXPay(new MyWXpayConfig());
        data.put("body","测试订单");
        data.put("out_trade_no",new Date().getTime()+"");
        data.put("notify_url", "https://uc.aaroom.cc");
        data.put("trade_type", "JSAPI");
        data.put("total_fee", "0.01");
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        data.put("spbill_create_ip",ip);
        Map<String, String> stringStringMap = wxPay.unifiedOrder(data);
        return stringStringMap;

    }

    @GetMapping("/WeChat/console/api/getCityNameByLatLng")
    public Object getCityNameByLatLng(@RequestParam(value = "lng")String lng,
                                      @RequestParam(value = "lat") String lat) {
        Map<String, Object> resultMap = new HashMap<>();
        String result = "";
        //测试数据格式
        // https://apis.map.qq.com/ws/geocoder/v1/?location=39.984154,116.307490&key=HXXBZ-NM7WJ-FH3FA-KHY6Y-B4ONF-APFJ7
        // 参数解释：lng：经度，lat：维度。KEY：腾讯地图key，get_poi：返回状态。1返回，0不返回
        String urlString = TENCENTMAP + lat + "," + lng + "&key=" + TENCENTKEY + "&get_poi=1";
        String location = TencentMapAPIUtils.getLocation(urlString);
        // 转JSON格式
        JSONObject jsonObject = JSONObject.fromObject(location).getJSONObject("result"); // 获取地址（行政区划信息） 包含有国籍，省份，城市
        if (jsonObject.size() != 0){
            JSONObject adInfo = jsonObject.getJSONObject("ad_info");
            resultMap.put("city", adInfo.get("city"));
        }else {
            resultMap = JSONObject.fromObject(result);
        }
        return resultMap;
    }

//    @GetMapping("/WeChat/console/api/queryHotelList")
   /* public Object queryHotelList(@RequestParam(value = "cityName",defaultValue = "青岛",required = false)String cityName,
                                 @RequestParam(value = "beginDate")String beginDate,
                                 @RequestParam(value = "endDate")String endDate,
                                 @RequestParam(value = "sortOpt",defaultValue = "",required = false)Integer sortOpt,
                                 @RequestParam(value = "sortType",defaultValue = "",required = false)Integer sortType) {
        //sortType : 默认空，2：价格3：距离 1：不知道
        //.按距离：1：远到近排序2：近到远排序
        //.按价格：1：高到低 2：低到高
        Map<String,Object> ret = new LinkedHashMap();
        ret.put("hotelList",new ArrayList<>());

        //1.获取所有aaroom酒店
        Map param = new HashMap();
        param.put("cityName",cityName);
        List<Map<String, Object>> hotelList = hotelBaseService.queryHotelList(param);
        if (hotelList.size()==0){
            ret.put("hotelCount ",0);
            return ret;
        }

        List usefulHotelList = new ArrayList(); //存储所有可用酒店
        for (Map<String, Object> hotel:hotelList){
            String smhotel_id = (String)hotel.get("smhotel_id");

            //2.通过OTA的api校验用户指定的入住时间段内是否有可用酒店房间
            Map<String, String> params = new HashMap<>();
            params.put("hotel_code",smhotel_id);
            //日期校验：最近30天的数据
            params.put("start",beginDate);
            params.put("end",endDate);
            String hotelPriceStr = shangmeiOTAService.exchange(AVAIL, HttpMethod.POST, params, String.class);
            System.out.println(hotelPriceStr);
            Gson gson = new Gson();
            Map hotelPriceMap = gson.fromJson(hotelPriceStr,Map.class);
            //查询当前酒店所有房型是否全部可用，全部可用房型为0，则不推荐此酒店
            List<Map> priceList = (List) hotelPriceMap.get("prices");
            List tempList = new ArrayList();
            List roomPriceList = new ArrayList();
            int usefulRoom =0 ;
            for (Map map:priceList){
                List<Map> room_type_prices = (List<Map>)map.get("room_type_prices");
                Map finalmap = room_type_prices.get(0);
                int num = (int)finalmap.get("num"); //此房型的可用数量
                int price =(int) finalmap.get("price");//此房型房价
                usefulRoom+=num;
                tempList.add(num);
                roomPriceList.add(price);
            }
            if (!tempList.contains(0) && tempList.size()>0){
                usefulHotelList.add(smhotel_id);//说明此酒店有可用房间，添加到可用酒店列表中
                //找出此酒店所有房型最低价格
                Collections.sort(roomPriceList);
                List myList = (List)ret.get("hotelList");
                Map dataMap = new LinkedHashMap();
                dataMap.put("brandName","AA ROOM");
                dataMap.put("areaName",hotel.get("county"));
                dataMap.put("minPrice",roomPriceList.get(0));
                dataMap.put("hotelName",hotel.get("hotel_name"));
                dataMap.put("restMsg","仅剩下"+usefulRoom+"间客房");
                myList.add(dataMap);
            }
        }
        ret.put("hotelCount",usefulHotelList.size());
         //3.通过pms数据库查询酒店订房情况，房价信息(暂时不写)
        return ret;
    }*/
}
