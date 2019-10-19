package com.shangmei.beans;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DictCodeMap {
    private String id;

    private String dictid;

    private String code;

    private String name;

    private String position;

    private Integer status;

    private Integer flag;

    private String createby;

    private Date createtime;

    private String lastrepair;

    private Date lasttime;

    private String customerId;

    private BigDecimal orderByItem;

    private String pcode;

    private String memo;


}