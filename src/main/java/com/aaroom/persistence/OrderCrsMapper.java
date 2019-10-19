package com.aaroom.persistence;

import com.aaroom.beans.OrderCount;
import com.aaroom.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by liyp on 2019/3/19 0019.
 */
@Repository
public interface OrderCrsMapper {
    List<OrderView> getOrderList(@Param("beginTime") String beginTime,
                                 @Param("endTime")String endTime,
                                 @Param("param")String param,
                                 @Param("channel_name")String channel_name,
                                 @Param("status")String status,
                                 @Param("pay_status")Integer pay_status);

    OrderCount getOrderCount(@Param("beginTime") String beginTime,
                             @Param("endTime")String endTime,
                             @Param("param")String param,
                             @Param("channel_name")String channel_name,
                             @Param("status")String status,
                             @Param("pay_status")Integer pay_status);

    MemberView getMemberIdByChannelOrderNo(@Param("channel_order_no")String channel_order_no);

    Integer getOrderPayStatusByChannelOrderNo(@Param("channel_order_no") String channel_order_no);

    OrderParam getPrePayOrderInfo(@Param("channel_order_no") String channel_order_no);

    OrderCrsView getOrderCrsInfo(String channel_order_no);

    List<Price> getPriceListByChannelOrderNo(String channel_order_no);
}
