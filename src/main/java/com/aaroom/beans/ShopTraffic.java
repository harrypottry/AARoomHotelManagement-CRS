package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShopTraffic {
    private Integer id;

    private Integer type;

    private String traffic_name;

    private String shop_id;

    private String traffic_kilometer;

    private String traffic_comment;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}