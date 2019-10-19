package com.shangmei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liyp on 2019/1/20 0020.
 */
@Data
public class OrderInfoView implements Serializable{
    private static final long serialVersionUID = -2551585398203661145L;

    private String   roomName;  //房间名

    private String   roomStatus; //房态

    private String   roomType;  //房型

    private Double   price;  //房价
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date     startDate;  //开始日期
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date     endDate;   //结束日期
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date     orderDate;  //订单日期

    private String   bookStatus;  //订单状态

    private String   orderStatus;  //订单状态


}
