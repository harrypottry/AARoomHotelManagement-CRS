package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShopContacts {
    private Integer id;

    private String shop_id;

    private String contacts_name;

    private String contacts_tel;

    private String contacts_qq;

    private String contacts_email;

    private Integer contacts_job;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}