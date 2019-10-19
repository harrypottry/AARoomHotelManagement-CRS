package com.aaroom.model;

import com.aaroom.beans.Room;
import lombok.Data;

/**
 * Created by sosoda on 2019/1/17.
 */
@Data
public class RoomView {

    private Integer id;

    private String room_name;

    private Integer hotel_id;

    private String hotel_name;

    private Integer room_type_id;

    private String room_type_name;

    private Integer floor;

    private Integer status;

    private Byte is_active;

    public RoomView(){

    }

    public RoomView(Room room) {
        this.id = room.getId();
        this.room_name = room.getRoom_name();
        this.hotel_id = room.getHotel_id();
        this.room_type_id = room.getRoom_type_id();
        this.floor = room.getFloor();
        this.status = room.getStatus();
        this.is_active = room.getIs_active();
    }
}
