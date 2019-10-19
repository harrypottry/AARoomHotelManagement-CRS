package com.aaroom.service;

import com.aaroom.beans.OrderCount;
import com.aaroom.model.*;
import com.aaroom.persistence.OrderCrsMapper;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import com.aaroom.wechat.persistence.WxOrderMapper;
import com.aaroom.wechat.persistence.WxpayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyp on 2019/3/19 0019.
 */
@Service
public class OrderCrsService {

    @Autowired
    private OrderCrsMapper orderCrsMapper;

    @Autowired
    private WxpayMapper wxpayMapper;

    @Autowired
    private WxOrderMapper wxOrderMapper;


    public Map getOrderList(String beginTime, String endTime,
                            String param,
                            String channel_name, String status,
                            Integer pay_status, Integer pageNo, Integer pageSize) {
        Map<String ,Object> map = new HashMap<>();
        List<OrderView> data = orderCrsMapper.getOrderList(beginTime,endTime,param,channel_name,status,pay_status);
        Page<OrderView> page = PageUtils.getPageList(data, pageNo, pageSize);
        map.put("data",page);
        OrderCount orderCount = orderCrsMapper.getOrderCount(beginTime,endTime,param,channel_name,status,pay_status);
        if (orderCount.getRoomNight() != null && orderCount.getRoomPrice() != 0.0){
            double val = new BigDecimal(orderCount.getRoomPrice()/orderCount.getRoomNight()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            orderCount.setAvgPrice(val);
        }
        map.put("count",orderCount);
        return map;
    }

    public Map<String,Object> getMemberCard(String channel_order_no, Object entity) {
        Map<String,Object> map= (Map<String, Object>) entity;
        MemberView memberView = orderCrsMapper.getMemberIdByChannelOrderNo(channel_order_no);
        map.put("member",memberView);
        //价格展示处理
        List<Object> prices = (List<Object>) map.get("price_list");
        Integer roomNum = (Integer) map.get("room_num");
        List<Object> priceList = new ArrayList<>();
        for (Object price : prices) {
            Map<String,Object> priceMap= (Map<String, Object>) price;
            Integer  wkPrice = (Integer) priceMap.get("price");
            double msPrice = wkPrice/0.92;
            priceMap.put("price",wkPrice * roomNum);
            priceMap.put("befor_price",String.format("%.0f",msPrice));
            priceList.add(priceMap);
        }
        double price_total = 0d;
        for (Object obj : priceList) {
            Map<String,Integer> priceMap= (Map<String, Integer>) obj;
            Integer  price = priceMap.get("price");
            price_total += price;
        }
        map.put("price_total",price_total);
        map.put("price_list",priceList);
        return map;
    }

    public Integer getOrderPayStatusByChannelOrderNo(String channel_order_no) {
        return orderCrsMapper.getOrderPayStatusByChannelOrderNo(channel_order_no);
    }

    public OrderParam getPrePayOrderInfo(String channel_order_no) {
        OrderParam param = wxpayMapper.getOrderInfo(channel_order_no);
        List<Price> priceList = wxOrderMapper.getPriceListByChannelOrderNo(channel_order_no);
        param.setPrice_list(priceList);
        return  param;
    }

    public OrderCrsView getOrderCrsInfo(String channel_order_no) {
        OrderCrsView orderCrsView = orderCrsMapper.getOrderCrsInfo(channel_order_no);
        //查房价集合
        List<Price> priceList = orderCrsMapper.getPriceListByChannelOrderNo(channel_order_no);
        //遍历集合计算原价
        for (Price price : priceList) {
            double v = price.getPrice() / 0.92;
            price.setBefor_price(Math.floor(v));
        }
        orderCrsView.setPrice_list(priceList);
        orderCrsView.setMember(orderCrsMapper.getMemberIdByChannelOrderNo(channel_order_no));
        return orderCrsView;
    }
}
