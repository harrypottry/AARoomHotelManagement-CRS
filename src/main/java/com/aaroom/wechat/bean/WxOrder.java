package com.aaroom.wechat.bean;

import lombok.Data;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-13 15:28
 **/
@Data
public class WxOrder {

    private String id;

    private String body;

    private String outTradeNo;

    private String spbillCreateIp;

    private String feeType;

    private String openId;

    private String goodsTag;

    private String tradeType;

    private String totalFee;

    private Integer status;
}
