package com.aaroom.wechat.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MemberInvoice {
    private Integer id;

    private String member_id;

    private String invoice_rise;

    private String company_name;

    private String company_number;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    private String order_no;
}