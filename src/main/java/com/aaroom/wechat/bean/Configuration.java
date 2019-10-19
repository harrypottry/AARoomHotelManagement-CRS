package com.aaroom.wechat.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Configuration {
    private Integer id;

    private String name;

    private String data;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}