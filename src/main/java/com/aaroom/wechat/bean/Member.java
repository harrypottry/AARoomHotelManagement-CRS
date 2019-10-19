package com.aaroom.wechat.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-07 10:39
 **/
@Data
public class Member {

    private String id;

    private String regHotelId;

    private String phoneNum;

    private String name;

    private String sex;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date birthday;

    private String email;

    private String cardNum;

    private String cardLevelId;

    private String cardStatus;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date regDate;

    private String openMode;

    private String certificateType;

    private String certificateNum;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date effectiveDate;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date expiryDate;

    private String openId;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    private String province;

    private String city;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date promoteDate;

    private Double promotePrice;

    private String hotelOwner;
}
