package com.aaroom.beans;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Permission {
    private Integer id;

    private String name;

    private Integer type;

    private String url;

    private Integer parent_id;

    private String icon;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    private List<Permission> permissions;

}