package com.shangmei.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChkinAccount {
    private String id;

    private String shopid;


    private Date atime;


    private Date dtime;

    private String field;

    private String bookfieldno;

    private String teamno;

    private String fieldno;

    private String classid;

    private String staffid;

    private String custometype;

    private String openroomtype;


    private Date indatetime;

    private String rtid;

    private String rid;

    private BigDecimal mprc;

    private BigDecimal disprc;

    private Integer bednum;

    private Integer breakfastnum;

    private Integer guestnum;

    private Integer stayday;


    private Date leavetime;

    private Integer issecrecy;

    private Integer iscard;

    private Integer isintegral;

    private Integer isinvoice;

    private String invoicename;

    private String agreementid;

    private String seller;

    private String prefeid;

    private String prefename;


    private Date begindate;


    private Date enddate;


    private Date stopdate;

    private Integer isnosee;

    private BigDecimal deposit;

    private String payment01;

    private BigDecimal paymoney01;

    private String payment02;

    private BigDecimal paymoney02;

    private String payment03;

    private BigDecimal paymoney03;

    private String ysq;

    private Integer ifover;

    private String status;


    private Date overtime;

    private BigDecimal totalfee;

    private Integer flag;

    private String createby;


    private Date createtime;

    private String lastrepair;


    private Date lasttime;

    private Integer isofflinecredit;

    private String memo;

}