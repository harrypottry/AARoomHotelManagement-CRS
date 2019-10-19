package com.aaroom.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderCrsView {
    private String channel_order_no;//渠道订单号

    private String book_status_desc;//订单状态

    private String remark;//备注

    private String hotel_name;//酒店名称

    private String room_type_name;//房型名称

    private String order_time;//下单时间

    private String contact_name;//联系人

    private String room_num;//房数

    private String end;//结束日期

    private String room_type_code;//房型码

    private String channel_name;//渠道

    private String hotel_code;//分店号

    private String customer_name;//客人姓名

    private String contact_phone;//联系人电话

    private String start;//开始日期

    private String address;//地址

    private String arrival;//预留时间

    private List<Price> price_list;//房价列表

    private String price_total;//总价

    private MemberView member;//会员信息会员信息


}
