package com.aaroom.persistence;

import com.aaroom.beans.Room;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Integer id);
    //根据room_name找到对应的room对象
    Room selectByroom_name(@Param("room_name") String room_name, @Param("hotel_id") Integer hotel_id);
    //根据楼层找到对应的room对象
    List<Room> getRoomByfloor(@Param("floor") Integer floor, @Param("hotel_id") int hotel_id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);
    //根据酒店id得到对应酒店下的roomlist
    List<String>  getRoomIdByHotelId(String hotel_id);

    List getFloorlistByHotelId(int hotel_id);

    List getFloorroomHotelId(@Param("hotel_id") int hotel_id, @Param("floor") Integer floor);
    //根据酒店id和roomid 在room表找到唯一的roomtypeid

    Integer getRoomTypeIdByHotelAndId(@Param("hotel_id") int hotel_id, @Param("id") Integer id);

    List<Room> getRoomsByHotelId(@Param("hotel_id") Integer hotel_id);

    List<Map<String,Object>> suggestClothBooking(@Param("hotel_id") Integer hotel_id);
}