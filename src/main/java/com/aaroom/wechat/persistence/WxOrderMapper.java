package com.aaroom.wechat.persistence;

import com.aaroom.model.Price;
import com.aaroom.wechat.bean.Invoice;
import com.aaroom.wechat.bean.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by liyp on 2019/3/11 0011.
 */
@Repository
public interface WxOrderMapper {
    void insert(Order order);

    List<Order> getUsuallyHotelInfoByEmpAndStatus(Map<String,Object> params);

    void updateOrderStatus(@Param("channel_order_no") String channel_order_no,
                           @Param("order_no") String order_no,
                           @Param("hotel_id") String hotel_id);

    void updateOrderStatusFinally( @Param("order_no") String order_no);

    List<Order> queryOrderList(@Param("member_id") String member_id,
                               @Param("comment_status") Integer comment_status,
                               @Param("pay_status")Integer pay_status);

    Order queryOrderByOrerNo(String channel_order_no);

    void updateOrderPayStatus(@Param("member_id")String memberId, @Param("order_no")String orderId);

    void relationOrderAndInvoice(@Param("channel_order_no")String channel_order_no,@Param("invoice_id")Integer id);

    Integer queryRelationByChannel_order_no(String channel_order_no);

    Invoice queryInvoiceByid(Integer invoiceid);

    String getMemeberLevelById(String memberID);

    String queryMemberIdByOrderNo(String order_no);

    List<Order> getNotCancelOrder();

    void timingUpdateOrderStatus(@Param("order_no") String order_no,
                                 @Param("channel_order_no") String channel_order_no,
                                 @Param("book_status_desc") String book_status_desc,
                                 @Param("pay_status") Integer pay_status);

    String getOutTradeNo(String channel_order_no);

    double getTotalFee(@Param("out_trade_no") String out_trade_no,
                       @Param("order_no") String order_no);

    String getArrivalTime(String channel_order_no);

    String getOrderPayStatus(String out_trade_no);

    Integer getRoomNumByOrderNo(String order_no);

    void updateWXOrderPayStatus(String out_trade_no);

    void updateOrderNumber(@Param("order_no") String order_no,
                           @Param("channel_order_no") String channel_order_no);

    void insertPriceListRelChannelOrderNo(Price price);

    List<Price> getPriceListByChannelOrderNo(String channel_order_no);

    Integer getOrderPayStatusByChannelOrderNo(String channel_order_no);

    String getOrderNoByChannelOrderNo(@Param("channel_order_no") String channel_order_no);

    void updateOrderStatusByChannelOrderNo(String channel_order_no);
}
