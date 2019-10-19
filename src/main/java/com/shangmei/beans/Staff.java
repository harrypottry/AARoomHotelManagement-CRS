package com.shangmei.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Staff {
    private String id;

    private String shopid;

    private String staffid;

    private String staffname;

    private String sex;

    private String staffcard;

    private String psw;

    private String psw1;

    private String powergroup;

    private Integer maxroomdis;

    private Integer roomdis;

    private Integer maxgoodsdis;

    private String goodsdis;

    private Integer maxyh;

    private Integer maxml;

    private String telphone;

    private Integer shouyin;

    private Integer guazhang;

    private Integer youhui;

    private Integer daochu;

    private Integer dayin;

    private Integer zhekou;

    private Integer moling;

    private Integer status;

    private Integer oaStatus;

    private Integer flag;

    private Integer usbLogin;

    private String defaultshop;

    private String createby;

    private Date createtime;

    private String lastrepair;

    private Date lasttime;

    private String salt;

    private String macs;

    private String customerId;

    private Integer system;

    private Date tryoutdate;

    private Integer isadmin;

    private String phoneManageLogin;

    private String headSculptureUrl;

    private Integer ishandgold;
}