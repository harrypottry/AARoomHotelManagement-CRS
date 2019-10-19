package com.shangmei.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by liyp on 2019/1/11 0011.
 */
@Data
public class OrderStatusView implements Serializable{

    private static final long serialVersionUID = 8005867163884636935L;

    private  String  id;

    private  String   dictID;

    private  String   code;

    private  String   name;

}
