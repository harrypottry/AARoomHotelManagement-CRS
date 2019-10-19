package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class HotelEmployee {

    private Integer hotel_id;

    private Integer employee_id;

    private Integer type;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}