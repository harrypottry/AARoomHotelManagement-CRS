package com.shangmei.service;

import com.shangmei.model.OrderInfoView;
import com.shangmei.model.OrderStatusView;
import com.shangmei.persistence.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liyp on 2019/1/11 0011.
 */
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public List<OrderStatusView> getAllOrderStatusList() {

        return  orderMapper.getAllOrderStatusList();
    }

    public List<OrderInfoView> getAllOrderList(String shopID, Integer flag, Integer orderStatus) {
        return orderMapper.getAllOrderList(shopID,flag,orderStatus);
    }
}
