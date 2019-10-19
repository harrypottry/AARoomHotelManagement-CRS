package com.aaroom.model;

import lombok.Data;

/**
 * Created by Administrator on 2019/3/22 0022.
 */
@Data
public class Price {

    private String start;

    private String end;

    private double price;

    private double aa_plus_price;

    private double aa_pro_price;

    private String channel_order_no;

    private  double befor_price;
}
