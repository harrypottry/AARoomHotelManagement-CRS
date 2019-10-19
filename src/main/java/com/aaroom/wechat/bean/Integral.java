package com.aaroom.wechat.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-12 10:13
 **/
@Data
public class Integral {

    private String id;

    private String memberId;

    private Integer integralBudget;

    private String causes;

    //是否有入住（0：是、1：否）
    private Integer status;

    private String hotelName;

    private String relateOrder;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date creationTime;

    private Integer integralAll;

    private String remark;

    private Integer type;
}
