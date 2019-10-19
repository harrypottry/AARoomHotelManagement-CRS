package com.aaroom.service;

import com.aaroom.beans.HotelBase;
import com.aaroom.beans.Room;
import com.aaroom.beans.RoomType;
import com.aaroom.model.RoomView;
import com.aaroom.persistence.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private HotelBaseService hotelBaseService;

    //根据酒店id得到对应酒店下的roomlist
    public List<String> getRoomIdByHotelId(String hotel_id){
        return roomMapper.getRoomIdByHotelId(hotel_id);
    }

    public List getFloorlistByHotelId(int hotel_id){
        return roomMapper.getFloorlistByHotelId(hotel_id);
    }

    public List getFloorroomHotelId(int hotel_id,Integer floor){
        return roomMapper.getFloorroomHotelId(hotel_id,floor);
    }

    public Room getRoomById(Integer id) {
        return roomMapper.selectByPrimaryKey(id);
    }
    //根据room_name找到对应的roomid
    public Room getRoomByname(String room_name,Integer hotel_id) {
        return roomMapper.selectByroom_name(room_name,hotel_id);
    }
    //根据楼层找到对应的room对象
    public List<Room> getRoomByfloor(Integer floor,int hotel_id) {
        return roomMapper.getRoomByfloor(floor,hotel_id);
    }

    public Integer getRoomTypeIdByHotelAndId(int hotel_id,Integer id){
        return roomMapper.getRoomTypeIdByHotelAndId(hotel_id, id);
    }

    public void update(Room room) {
        roomMapper.updateByPrimaryKeySelective(room);
    }

    public List<Room> getRoomsByHotelId(Integer hotel_id) {
        return roomMapper.getRoomsByHotelId(hotel_id);
    }

    //TODO:感觉业务写在java中比较复杂，所以写在了sql里面，以后有可能再拆出来
    public List<Map<String, Object>> suggestClothBooking(Integer hotel_id) {

        return roomMapper.suggestClothBooking(hotel_id);
    }

    public RoomView getRoomView(Room room, Integer room_id){
        if(room ==null) {
            room = getRoomById(room_id);
            if(room == null) {
                return null;
            }
        }
        RoomView roomView = new RoomView(room);
        //房间类型
        RoomType roomType = roomTypeService.selectByPrimaryKey(roomView.getRoom_type_id());
        roomView.setRoom_type_name(roomType.getDesc());
        //所属酒店信息
        HotelBase hotelBase = hotelBaseService.getById(roomView.getHotel_id());
        roomView.setHotel_name(hotelBase.getHotel_name());
        return roomView;
    }

        //增加房子
    public Integer insert(Room room) {
        return roomMapper.insertSelective(room);
    }
}
