package com.shangmei.restcontroller;

import com.aaroom.exception.RestError;
import com.shangmei.model.OrderInfoView;
import com.shangmei.model.OrderStatusView;
import com.shangmei.service.OrderService;
import com.shangmei.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by liyp on 2019/1/11 0011.
 */
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    //获取所有的订单类型
    @GetMapping("/wx/console/api/getAllOrderStatusList")
    public Object  getAllOrderStatusList(){
        List<OrderStatusView> orderStatusList = orderService.getAllOrderStatusList();
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderStatusList",orderStatusList);
        return new RestError(map);
    }
    //获取所有的订单信息
    @GetMapping("/wx/console/api/getAllOrderList")
    public Object  getAllOrderList(@RequestParam String shopID,
                                   @RequestParam(required = false,defaultValue = "1") Integer flag,
                                   @RequestParam(required = false) Integer orderStatus){
        List<OrderInfoView> orderStatusList = orderService.getAllOrderList(shopID,flag,orderStatus);
        HashMap<String, Object> map = new HashMap<>();
        List<String> orderDateList = new ArrayList<>();
        for (OrderInfoView orderInfoView : orderStatusList) {
            if (!orderDateList.contains(orderInfoView.getOrderDate())){
                String date = DateUtil.getDate(orderInfoView.getOrderDate());
                orderDateList.add(date);
            }
        }
        for (String date : orderDateList) {
            List<OrderInfoView> orderDataList = new ArrayList<>();
                for (OrderInfoView infoView : orderStatusList) {
                    String dataDate = DateUtil.getDate(infoView.getOrderDate());
                    if (dataDate.equals(date)){
                        orderDataList.add(infoView);
                    }
                }
                map.put(date,orderDataList);
        }
        return new RestError(map);
    }
}
