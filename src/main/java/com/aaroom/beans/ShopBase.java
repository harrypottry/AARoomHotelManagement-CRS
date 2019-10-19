package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShopBase {
    private Integer id;

    private String province;

    private String city;

    private String area;

    private String shop_name;

    private String boss_name;  //业主名称

    private String shop_status;

    private String smhotel_code;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date online_time;

    private Integer pms_status;

    private String postal_code;

    private String address_detail;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date decorate_time;

    private String business_square;

    private String building_detail;

    private String hasforeign;

    private String channel;

    private String pay_method;

    private String shop_detail;

    private String bd_name;  //扩展bd

    private String travel_guide;

    private String warm_prompt;

    private String shop_service;

    private String hotel_brand;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    private String master_name;  //运营店长

    private String shop_comment;

    private String fax;

    private String tel;

    private Integer room_num;

    public ShopBase() {
    }

    public ShopBase(String shop_name) {
        this.shop_name = shop_name;
    }

    public ShopBase(String shop_name, String smhotel_code) {
        this.shop_name = shop_name;
        this.smhotel_code = smhotel_code;
    }
}