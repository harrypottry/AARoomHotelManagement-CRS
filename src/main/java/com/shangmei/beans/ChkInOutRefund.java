package com.shangmei.beans;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChkInOutRefund {
    private String teamNo;

    private String fieldNo;

    private String outPayType;

    private String openRoomTypeName;

    private String rtName;

    private String rname;

    private String guestName;

    private String customeTypeName;

    private String agreementname;

    private Date indateTime;

    private Date overTime;

    private BigDecimal overnightDays;

    private BigDecimal disprc;

    private BigDecimal yuShouQuan;

    private BigDecimal deposit;

    private BigDecimal roomfee;

    private BigDecimal otherFee;

    private BigDecimal totalfee;

    private BigDecimal xianJin;

    private BigDecimal yinHangKa;

    private BigDecimal yuShouQuanOK;

    private BigDecimal wangFu;

    private BigDecimal xieYiGuaZhang;

    private BigDecimal diYongQuan;

    private BigDecimal chuZhiKa;

    private BigDecimal dianZiQuan;

    private BigDecimal jiFen;

    private BigDecimal huiKuan;

    private BigDecimal zhiPiao;

    private BigDecimal xiaoJi;

    private String staffName;

    private Date lastTime;

    private String className;
}
