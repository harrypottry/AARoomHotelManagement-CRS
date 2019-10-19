package com.aaroom.restcontroller;

import com.aaroom.model.OrderCrsView;
import com.aaroom.model.OrderParam;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.OrderCrsService;
import com.aaroom.service.ShangmeiOTAService;
import com.aaroom.utils.Constants;
import com.aaroom.wechat.service.WechatOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyp on 2019/3/18 0018.
 */
@RestController
public class OrderCrsController {

    @Autowired
    private OrderCrsService orderCrsService;

    @Autowired
    private ShangmeiOTAService shangmeiOTAService;

    //订单列表
    @GetMapping("/console/api/getOrderList")
    public Map<String,Object> getOrderList(@RequestParam(required = false) String beginTime,
                               @RequestParam(required = false) String endTime,
                               @RequestParam(required = false) String param,
                               @RequestParam(required = false) String channel_name,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) Integer pay_status,
                               @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                               @RequestParam(required = false, defaultValue = "20") Integer pageSize){
        Map<String,Object> result = orderCrsService.getOrderList(beginTime,endTime,param,channel_name,status,pay_status,pageNo,pageSize);
        return result;
    }
    //获取订单详情
    @GetMapping("/console/api/getOrderInfo")
    public ShangmeiRetView getOrderInfo(@RequestParam String channel_order_no){
        Map<String, String> params = new HashMap<>();
        params.put("channel_order_no",channel_order_no);
        //查订单状态
        ShangmeiRetView exchange = new ShangmeiRetView();
        Integer payStatus = orderCrsService.getOrderPayStatusByChannelOrderNo(channel_order_no);
        if (payStatus == 1  || payStatus == 5){
            //返回预订单信息
            OrderCrsView orderCrsView = orderCrsService.getOrderCrsInfo(channel_order_no);
            exchange.setEntity(orderCrsView);
            exchange.setError_msg("success");
            exchange.setError_debug("");
            exchange.setError_code(0);
            return exchange;
        }
        exchange = shangmeiOTAService.exchange(Constants.ORDER_QUERY, HttpMethod.POST, params, ShangmeiRetView.class);
        //获取会员卡信息
        if (exchange.getError_code()==0){
            Map<String,Object> map = orderCrsService.getMemberCard(channel_order_no,exchange.getEntity());
            exchange.setEntity(map);
        }
        return exchange;
    }
}
