package com.aaroom.wechat.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-08 16:37
 **/
@Data
public class MemberLevel {

    private String id;

    private String levelName;

    private String levelNum;

    private String upLevel;

    private String downLevel;

    private Double discountPrice;

    private String salePrice;

    private Double integralTimes;

    private String promoteJy;//升级间夜

    private String integralPromote;

    private String invatePromote;

    private Integer meanwhileIptimizeNum;

    private String preRetain;

    private String delay;

    private String integralExchange;

    private Integer birthIntegral;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}
