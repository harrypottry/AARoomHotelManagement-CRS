package com.shangmei.beans;

import lombok.Data;

import java.util.Date;

@Data
public class ShopBase {
    private String id;

    private String shopid;

    private String shopname;

    private Date createdate;

    private String areaid;

    private String provinceid;

    private String cityid;

    private String countyid;

    private String streetaddress;

    private String shoptype;

    private String status;

    private String brandid;

    private Integer flag;

    private String auditStatus;

    private String createby;

    private Date createtime;

    private String lastrepair;

    private Date lasttime;

    private String customerId;

    private Integer system;

    private Date startdate;

    private Date qydate;

    private Date jydate;

    private Date rundate;

    private Date stopdate;

    private Integer powernum;

    private Date authorizedate;

    private Integer isusing;

    private Integer optFlag;

    private Integer offlinecredit;

}