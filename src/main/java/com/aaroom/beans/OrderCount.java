package com.aaroom.beans;

import lombok.Data;

/**
 * Created by liyp on 2019/3/19 0019.
 */
@Data
public class OrderCount {

    private  Integer  OrderSum;//订单数量

    private  Integer  roomNight;//间夜总数

    private  double  roomPrice;//房费总价

    private  double  avgPrice;//平均房价
}
