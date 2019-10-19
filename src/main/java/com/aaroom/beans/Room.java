package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Room {
    private Integer id;

    private String room_name;

    private Integer hotel_id;

    private Integer room_type_id;

    private Integer floor;

    private Integer status;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;
}