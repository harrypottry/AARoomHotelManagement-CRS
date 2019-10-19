package com.shangmei.service;

import com.shangmei.persistence.RoomInfoMapper;
import com.shangmei.model.RoomView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liyp on 2019/1/9 0009.
 */
@Service
public class RoomInfoService {
    @Autowired
    private RoomInfoMapper roomMapper;
    @Transactional(readOnly = true)
    public List<RoomView> getRoomList( String shopID,String roomType,String roomStatus) {
        return roomMapper.getRoomList(shopID,roomType,roomStatus);
    }
    @Transactional(readOnly = true)
    public List<RoomView> getRoomStatusByShopID(String shopID) {
        return roomMapper.getRoomStatusByShopID(shopID);
    }
    @Transactional(readOnly = true)
    public List<RoomView> getRoomTypeByShopID(String shopID) {
        return roomMapper.getRoomTypeByShopID(shopID);
    }
}
