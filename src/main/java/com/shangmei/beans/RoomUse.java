package com.shangmei.beans;

import lombok.Data;

import java.util.Date;

@Data
public class RoomUse {
    private String id;

    private Date atime;

    private String shopid;

    private String rtid;

    private String rtname;

    private Integer roomcounts;

    private Integer sellnum;

    private Integer nousenum;

    private Integer nullnum;

    private Integer repairnum;

    private Integer freenum;

    private Integer mynum;

    private Double roomrate;

    private Double rentalnum;

    private Double roomavgfee;

    private Double money;

    private String p04;

    private String p05;

    private Integer flag;

    private Date createtime;

}