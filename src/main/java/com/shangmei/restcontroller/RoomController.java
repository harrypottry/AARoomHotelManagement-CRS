package com.shangmei.restcontroller;


import com.aaroom.beans.Room;
import com.aaroom.exception.RestError;
import com.aaroom.service.RoomService;
import com.shangmei.model.RoomView;
import com.shangmei.service.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyp on 2019/1/9 0009.
 */
@RestController
public class RoomController{
    @Autowired
    private RoomInfoService roomService;

    @Autowired
    private RoomService AAroomService;

    @GetMapping("/wx/console/api/getRoomListByShopID")
    public Object getRoomListByShopID(@RequestParam String shopID,
                                      @RequestParam(required = false,defaultValue ="") String roomType,
                                      @RequestParam(required = false,defaultValue ="") String roomStatus){
        List<RoomView> roomList = roomService.getRoomList(shopID, roomType, roomStatus);
        Map<Integer,Object> map = new HashMap<>();
        List<Integer> floorList = new ArrayList<>();
        for (RoomView roomView : roomList) {
            if (!floorList.contains(roomView.getFloorNum())){
                floorList.add(roomView.getFloorNum());
            }
        }
        for(Integer floorNum :floorList) {
            List<RoomView> roomListByFloor = new ArrayList<>();
            for (RoomView roomView : roomList) {
                if (roomView.getFloorNum() == floorNum) {
                        roomListByFloor.add(roomView);
                    }
                }
            map.put(floorNum,roomListByFloor);
            }

        return new RestError(map);
    }
    @GetMapping("/wx/console/api/getRoomStatusByShopID")
    public Object getRoomStatusByShopID(@RequestParam String shopID){

        List<RoomView> roomStatusByShopID = roomService.getRoomStatusByShopID(shopID);
        return new RestError(roomStatusByShopID);
    }
    @GetMapping("/wx/console/api/getRoomTypeByShopID")
    public Object getRoomTypeByShopID(@RequestParam String shopID){

        List<RoomView> roomTypeByShopID = roomService.getRoomTypeByShopID(shopID);
        return new RestError(roomTypeByShopID);
    }

    @PostMapping("/console/api/postRoomStatus")
    public RestError postRoomStatus(HttpServletRequest request,
                                    Integer room_id,
                                    Integer status){
        Integer employeeId = (Integer)request.getSession().getAttribute("employee_id");
        //TODO：判断此房间是否为员工绑定的酒店下的房间
        Room room = new Room();
        room.setId(room_id);
        room.setStatus(status);
        AAroomService.update(room);
        return RestError.E_OK;
    }


 }
