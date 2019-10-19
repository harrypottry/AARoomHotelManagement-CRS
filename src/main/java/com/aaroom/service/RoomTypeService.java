package com.aaroom.service;

import com.aaroom.beans.RoomType;
import com.aaroom.persistence.RoomTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    public RoomType selectByPrimaryKey(Integer id){
        return roomTypeMapper.selectByPrimaryKey(id);
    }

    public Integer getTotalByRoomTypeId(Integer room_type_id){
        return roomTypeMapper.getTotalByRoomTypeId(room_type_id);
    }
    //根据roomtypeid查到roomtype对象
    public RoomType getRoomTypeById(Integer room_type_id){
        return roomTypeMapper.getRoomTypeById(room_type_id);
    }

    public RoomType getRoomTypeByEmpId(Integer room_type_id){
        return roomTypeMapper.getRoomTypeByEmpId(room_type_id);
    }
}
