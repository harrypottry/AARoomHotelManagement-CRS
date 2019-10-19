package com.aaroom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-14 14:33
 **/
@Data
public class MemberListView {

    private String id;

    private String cardLevel;

    private String cardNum;

    private String name;

    private String phoneNum;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date regDate;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date promoteDate;

    private Double promotePrice;

    private String openMode;

    private String regHotelId;

    private String hotelName;

    private String province;

    private String city;

    private String hotelOwern;

    private Integer intrgralAll;
}
