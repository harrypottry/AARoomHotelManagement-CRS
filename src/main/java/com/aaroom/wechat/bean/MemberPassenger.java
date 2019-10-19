package com.aaroom.wechat.bean;

import lombok.Data;

import java.util.Date;

@Data
public class MemberPassenger {
    private Integer id;

    private String member_id;

    private String name;

    private String phone;

    private String email;

    private String card_type;

    private String card_number;

    private Date createTime;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;

    private Byte isActive;

}