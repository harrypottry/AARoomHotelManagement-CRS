package com.aaroom.wechat.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MemberComment {
    private Integer id;

    private String member_id;

    private String hotel_id;

    private String order_id;

    private String utility;

    private String comment_text;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}