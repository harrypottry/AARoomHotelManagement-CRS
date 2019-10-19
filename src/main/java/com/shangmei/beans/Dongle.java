package com.shangmei.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Dongle {
    private String id;

    private String customerId;

    private String shopId;

    private String pid;

    private String userPin;

    private String soPin;

    private String secretKey;

    private Integer flag;

    private String createBy;

    private Date createTime;

    private String lastRepair;

    private Date lastTime;

    private String pidSeed;

    private String soPinSeed;

    private String sn;

    private String storeData;
}