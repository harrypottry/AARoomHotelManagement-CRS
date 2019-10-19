package com.aaroom.wechat.bean;

import lombok.Data;

import java.util.Date;

@Data
public class MemberAddress {
    private Integer id;

    private String member_id;

    private String name;

    private String phone;

    private String province;

    private String city;

    private String area;

    private String address_detail;

    private Integer postal_code;

    private Date createTime;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;

    private Byte isActive;


    public MemberAddress() {
    }

    public MemberAddress(Integer id) {
        this.id = id;
    }
}