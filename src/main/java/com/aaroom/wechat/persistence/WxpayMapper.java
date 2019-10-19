package com.aaroom.wechat.persistence;

import com.aaroom.model.OrderParam;
import com.aaroom.wechat.bean.WxOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WxpayMapper {

    void insertOrderData(@Param(value = "wo")WxOrder wxOrder);

    void updateOrderStatus(@Param(value = "orderId")String orderId);

    WxOrder queryWXOrderByOrderId(@Param(value = "orderId")String orderId);

    String queryWXorderByTradeNo(String out_trade_no);

    OrderParam getOrderInfo(String order_no);
}
