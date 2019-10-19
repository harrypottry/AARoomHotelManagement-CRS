package com.aaroom.model;

import lombok.Data;

/**
 * Created by liyp on 2019/3/19 0019.
 */
@Data
public class OrderView {

    private  String  channel_order_no;//订单号

    private  String  channel_name;//预定来源

    private  String  hotel_name;//分店名称

    private  String  hotel_code;//分店号

    private  String  room_type_name;//房型

    private  String  room_num;//间数

    private  String  price_total;//总价

    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String order_time;//下单时间

    //@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private String begin_time;//开始时间

    //@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private String end_time;//结束时间

    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String arrival_time;//留房截止

    private String status;//订单状态

    private String address;//酒店地址

    private Integer pay_status;//支付状态1待支付2待评价3已评价4已支付5已取消

    private Integer room_night;//间夜数

    private String  member_level;//会员等级

    private String  member_name;//会员姓名

    private String  phone_num;//会员电话
}
