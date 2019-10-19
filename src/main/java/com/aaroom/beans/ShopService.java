package com.aaroom.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ShopService {
    private Integer id;

    private String wifi_name;

    private String shop_id;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date create_time;

    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

    private String wifi_password;

}