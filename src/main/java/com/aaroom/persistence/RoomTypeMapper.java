package com.aaroom.persistence;

import com.aaroom.beans.RoomType;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomType record);

    int insertSelective(RoomType record);

    RoomType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomType record);

    int updateByPrimaryKey(RoomType record);

    int getTotalByRoomTypeId(Integer room_type_id);
    //根据roomtypeid查到roomtype对象
    RoomType getRoomTypeById(Integer room_type_id);

    RoomType getRoomTypeByEmpId(Integer room_type_id);
}