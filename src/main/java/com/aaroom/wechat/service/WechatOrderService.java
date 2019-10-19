package com.aaroom.wechat.service;


import com.aaroom.model.OrderParam;
import com.aaroom.model.Price;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.ShangmeiOTAService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Constants;
import com.aaroom.wechat.bean.Invoice;
import com.aaroom.wechat.bean.Order;
import com.aaroom.wechat.persistence.WxOrderMapper;
import com.aaroom.wechat.persistence.WxpayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by sosoda on 2019/2/20.
 */
@Service
public class WechatOrderService {


    @Value("${shangmei.ota.clientid}")
    private String clientid;


    @Value("${shangmei.ota.clientsecret}")
    private String clientsecret;

    @Autowired
    private WxOrderMapper wxOrderMapper;


    @Autowired
    private ShangmeiOTAService shangmeiOTAService;

    @Autowired
    private WxpayMapper wxpayMapper;


    //在aa数据库中保存相应的订单数据
    public String insertOrder(OrderParam param) {
            //把订单实体中的需要的数据取出放入aa订单实体当中
            Order order = new Order();
            order.setPrice_total(param.getPrice_total().toString());
            order.setChannel_order_no(param.getChannel_order_no());
            order.setChannel_name(Constants.WX_CHANNEL_NAME);
            order.setHotel_code(param.getHotel_code());
            order.setHotel_name(param.getHotel_name());
            order.setMember_id(param.getMemberId());
            order.setCustomer_name(param.getCustomer_name());
            order.setContact_name(param.getContact_name());
            order.setContact_phone(param.getContact_phone());
            order.setRoom_type_name(param.getRoom_type_name());
            order.setRoom_type_code(param.getRoom_type_code());
            order.setRoom_num(param.getRoom_num().toString());
            order.setOrder_time(CommonUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
            order.setBegin_time(param.getStart());
            order.setEnd_time(param.getEnd());
            order.setPrice_code(param.getPriceCode());
            order.setArrival_time(param.getArrival());
            order.setRemark(param.getRemark());
            order.setAddress(param.getAddress());
            Date startTime = CommonUtil.strToDate(order.getBegin_time(),"yyyy-MM-dd");
            Date endTime = CommonUtil.strToDate(order.getEnd_time(),"yyyy-MM-dd");
            long room_night = (endTime.getTime() - startTime.getTime())/(1000*3600*24);
            order.setRoom_night(room_night * param.getRoom_num());
            //在价格表中记录
            List<Price> price_list = param.getPrice_list();
            for (Price price : price_list) {
                price.setChannel_order_no(param.getChannel_order_no());
                wxOrderMapper.insertPriceListRelChannelOrderNo(price);
            }
            //是否需要开发票
            if (param.getHasInvoice()!= 0 && param.getInvoiceId() != null){
                //将发票信息和订单进行关联
                order.setInvoce_id(param.getInvoiceId().toString());
                wxOrderMapper.relationOrderAndInvoice(order.getChannel_order_no(),param.getInvoiceId());
            }
            //在aa数据库增加信息
            wxOrderMapper.insert(order);
        return param.getChannel_order_no();
    }

    public Map<String, Integer> getUsuallyHotelInfoByEmpAndStatus(List<Order> orderList){
            Map<String,Integer> numMap = new HashMap<>();
            for (Order order:orderList){
                numMap.put(order.getHotel_code(),numMap.get(order.getHotel_code())==null?1:numMap.get(order.getHotel_code())+1);
            }
            List<Map.Entry<String,Integer>> lists = new ArrayList<>(numMap.entrySet());
            Map<String, Integer> sortedMap = new LinkedHashMap<>();
            Collections.sort(lists, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    Integer q1 = o1.getValue();
                    Integer q2 = o2.getValue();
                    Integer p = q2 -q1;
                    if (p>0){
                        return 1;
                    }else if (p==0){
                        return 0;
                    }else
                        return -1;
                }
            });
            if (lists.size()>=10){
                //lists.subList()用法
                for (Map.Entry<String,Integer> set:lists.subList(0,10)){
                    sortedMap.put(set.getKey(),set.getValue());
                }
            }else {
                for (Map.Entry<String, Integer> set:lists){
                    sortedMap.put(set.getKey(),set.getValue());
                }
            }
            return sortedMap;
    }

    public List<Order> getUsuallyHotelInfo(Map<String,Object> params){
        return wxOrderMapper.getUsuallyHotelInfoByEmpAndStatus(params);
    }
    //同步尚美数据库 订单状态
    public void updateOrderStatus(Map<String, String> params) {
        String channel_order_no = params.get("channel_order_no");
        String order_no = params.get("order_no");
        String hotel_id = params.get("hotel_code");
        wxOrderMapper.updateOrderStatus(channel_order_no,order_no,hotel_id);
    }

    public void updateOrderStatusFinally(String order_no){
        wxOrderMapper.updateOrderStatusFinally(order_no);
    }
    //查询当前会员的所有订单信息
    public List<Order> queryOrderList(String member_id, Integer comment_status,Integer pay_status){
       return wxOrderMapper.queryOrderList(member_id,comment_status,pay_status);
    }
    //计算订单价格
    public long computeHotelFee(String order_no) {
        //查询订单信息 获得订单总价
        Order order = wxOrderMapper.queryOrderByOrerNo(order_no);
        long priceTotal = (long) Double.parseDouble(order.getPrice_total());
        return priceTotal*100;
    }

    public Invoice queryRelationByChannel_order_no(String channel_order_no) {
        //查询关联表
        Integer invoiceid = wxOrderMapper.queryRelationByChannel_order_no(channel_order_no);
        //获取发票信息
        return wxOrderMapper.queryInvoiceByid(invoiceid);
    }

    public Map<String, Object> countRoomTypePrice(Object entity) {
        Map<String,Object> map= (Map<String, Object>) entity;
        Object prices =map.get("prices");
        List<Object> priceList = (List<Object>) prices;
        ArrayList<Object> arrayList = new ArrayList<>();
        ArrayList<Object> arrayList2 = new ArrayList<>();
        //遍历价格列表集合
        for (Object price : priceList) {
            Map<String,Object> priceMap= (Map<String, Object>) price;
            Object roomTypePrices =priceMap.get("room_type_prices");
            List<Object> roomTypePriceList = (List<Object>) roomTypePrices;
            List<Integer> numList = new ArrayList<>();
            for (Object obj : roomTypePriceList) {
                Map<String,Object> objMap = (Map<String, Object>)obj;
                objMap.put("price",objMap.get("Wprc") );//当天的网客价
                objMap.put("aa_plus_price",objMap.get("Yprc") );//aa_plus会员88折
                objMap.put("aa_pro_price", objMap.get("Gprc"));//aa_pro会员85折
                arrayList.add(objMap);
                //取最小房间数
                numList.add((Integer) objMap.get("num"));
                int min = numList.get(0);
                for (int i = 0 ; i < numList.size();i++){
                    if(min > numList.get(i)){
                        min = numList.get(i);
                    }
                }
                priceMap.put("minNum",min);
            }
            arrayList2.add(priceMap);
        }
        map.put("prices",arrayList2);
        return  map;
    }
    //验证订单状态
    public boolean isCheckIn(ShangmeiRetView ret) {
        Object entity = ret.getEntity();
        Map<String,String> resultMap = (Map<String, String>) entity;
        String book_status = resultMap.get("book_status");
        //如果订单状态已转入住 或者部分转入住 无法退房
        if (book_status.equals("581d28635aa411e598f2d8cb8a2f9c07") || book_status.equals("581d2ac35aa411e598f2d8cb8a2f9c07")){
            return true;
        }
        return false;
    }

    public boolean isCencel(ShangmeiRetView ret) {
        Object entity = ret.getEntity();
        Map<String,String> resultMap = (Map<String, String>) entity;
        String book_status = resultMap.get("book_status");
        //如果订单状态已取消
        if (book_status.equals("581d23bf5aa411e598f2d8cb8a2f9c07")){
            return true;
        }
        return false;
    }

    public Map<String,Object> queryRoomTypePrice(Object entity,String order_no) {
        //查订单定了几间房
        Integer roomNum = wxOrderMapper.getRoomNumByOrderNo(order_no);
        Map<String,Object> map= (Map<String, Object>) entity;
        List<Object> prices = (List<Object>) map.get("price_list");
        List<Object> priceList = new ArrayList<>();
        for (Object price : prices) {
            Map<String,Object> priceMap= (Map<String, Object>) price;
            Integer  wkPrice = (Integer) priceMap.get("price");
            priceMap.put("price",wkPrice*roomNum);
            priceList.add(priceMap);
        }
        double price_total = 0d;
        for (Object obj : priceList) {
            Map<String,Object> priceMap= (Map<String, Object>) obj;
            Object  priceStr = priceMap.get("price");
            Integer price = (Integer) priceStr;
            price_total += price;
        }
        map.put("price_total",price_total);
        map.put("price_list",priceList);
        return  map;
    }
    //定时更新订单状态
    public void timingUpdateOrderStatus() {
        //查询本地数据库的未被取消的订单
        List<Order> orders = wxOrderMapper.getNotCancelOrder();
        //遍历集合
        for (Order order : orders) {
            //查询尚美的订单状态
            Map<String, String> params = new HashMap<>();
            params.put("order_no",order.getOrder_no());
            params.put("channel_order_no",order.getChannel_order_no());
            ShangmeiRetView exchange = shangmeiOTAService.exchange(Constants.ORDER_QUERY, HttpMethod.POST, params, ShangmeiRetView.class);
            if (exchange.getError_code()==0){
                //同步状态
                Map<String,String> entity = (Map<String, String>) exchange.getEntity();
                String book_status_desc = entity.get("book_status_desc");
                Integer pay_status = null;
                if(book_status_desc.equals("已转入住")) {
                    pay_status = 2;
                    wxOrderMapper.timingUpdateOrderStatus(order.getOrder_no(), order.getChannel_order_no(), book_status_desc,pay_status);
                }else if(book_status_desc.equals("取消")){
                    pay_status = 5;
                    wxOrderMapper.timingUpdateOrderStatus(order.getOrder_no(), order.getChannel_order_no(), book_status_desc,pay_status);
                }else {
                    wxOrderMapper.timingUpdateOrderStatus(order.getOrder_no(), order.getChannel_order_no(), book_status_desc,pay_status);
                }
            }
        }
    }

    public String getOutTradeNo(String order_no) {

        return wxOrderMapper.getOutTradeNo(order_no);
    }

    public String getArrivalTime(String channel_order_no) {
        return wxOrderMapper.getArrivalTime(channel_order_no);
    }

    public String getOrderPayStatus(String out_trade_no) {
        return wxOrderMapper.getOrderPayStatus(out_trade_no);
    }

    public void updateWXOrderPayStatus(String out_trade_no) {
        wxOrderMapper.updateWXOrderPayStatus(out_trade_no);
    }

    public void updateOrderNumber(Object exchange) {
        Map<String,String> map = (Map<String, String>) exchange;
        String order_no = map.get("order_no");
        wxOrderMapper.updateOrderNumber(order_no,map.get("channel_order_no"));

    }

    public Integer getOrderPayStatusByChannelOrderNo(String channel_order_no) {
        return wxOrderMapper.getOrderPayStatusByChannelOrderNo(channel_order_no);
    }

    public OrderParam getPrePayOrderInfo(String channel_order_no) {
        OrderParam param = wxpayMapper.getOrderInfo(channel_order_no);
        List<Price> priceList = wxOrderMapper.getPriceListByChannelOrderNo(channel_order_no);
        param.setPrice_list(priceList);
        return  param;
    }

    public String getOrderNoByChannelOrderNo(String channel_order_no) {
        return  wxOrderMapper.getOrderNoByChannelOrderNo(channel_order_no);
    }

    public void updateOrderStatusByChannelOrderNo(String channel_order_no) {
        wxOrderMapper.updateOrderStatusByChannelOrderNo(channel_order_no);
    }
}
