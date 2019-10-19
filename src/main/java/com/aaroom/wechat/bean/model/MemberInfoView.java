package com.aaroom.wechat.bean.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-14 11:05
 **/
@Data
public class MemberInfoView {

    private String id;

    private String cardNum;

    private String cardLevel;

    private String levelNum;

    private String name;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date birthday;

    private String phoneNum;

    private String certificateNum;

    private String email;

    private Double discountPrice;

    private Integer giveType;
}
