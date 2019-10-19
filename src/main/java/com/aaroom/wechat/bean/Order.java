package com.aaroom.wechat.bean;

import com.sun.org.apache.regexp.internal.RE;
import lombok.Data;

/**
 * Created by liyp on 2019/3/11 0011.
 */
@Data
public class Order {
    private  String  id;

    private  String  order_no;//订单号

    private  String  channel_order_no;//渠道订单号

    private  String  channel_name;//预定来源

    private  String  hotel_name;//分店名称

    private  String  hotel_code;//分店号

    private  String  member_id;//会员id

    private  String  room_type_name;//房型

    private  String  room_type_code;//房型码

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

    private Integer comment_status;//评论状态1已评论0未评论

    private Integer pay_status;//支付状态1已支付0未支付

    private Long room_night;//间夜数

    private String customer_name;//入住姓名;

    private String contact_name ;//入住姓名;

    private String contact_phone;//联系人电话;

    private String invoce_id;

    private String price_code;

    private Integer hasInvoice;

    private String remark;

}
