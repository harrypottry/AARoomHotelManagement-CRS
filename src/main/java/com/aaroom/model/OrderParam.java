package com.aaroom.model;

import lombok.Data;

import java.util.List;

/**
 * Created by liyp on 2019/3/22 0022.
 */
@Data
public class OrderParam {
    private String channel_order_no;
    private String arrival;
    private String contact_name;
    private String contact_phone;
    private String customer_name;
    private String end;
    private String hotel_code;
    private String address;
    private Integer pay_status;
    private List<Price> price_list;
    private Double price_total;
    private String remark;
    private Integer room_num;
    private String room_type_code;
    private String room_type_name;
    private String start;
    private Integer invoiceId;
    private String memberId;
    private Integer hasInvoice;
    private String  priceCode;
    private String  hotel_name;
}
