package com.aaroom.wechat.controller;

import com.aaroom.wechat.service.WechatOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liyp on 2019/3/26 0026.
 */
@Component
public class OrderStatusTask {

    @Autowired
    private WechatOrderService wechatOrderService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void updateOrderStatus(){
        System.out.println("==============定时器生效================");
        wechatOrderService.timingUpdateOrderStatus();
    }
}
