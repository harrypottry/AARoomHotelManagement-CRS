package com.aaroom.wechat.bean;

import lombok.Data;

import java.util.Date;

@Data
public class MemberCollection {
    private Integer id;

    private String hotel_id;

    private String member_id;

    private Date createTime;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;

    private Byte isActive;


    public MemberCollection() {
    }

    public MemberCollection(String hotel_id, String member_id) {
        this.hotel_id = hotel_id;
        this.member_id = member_id;
    }
}