package com.shangmei.service;

import com.shangmei.persistence.RoomUseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RoomUseService {
    @Resource
    private RoomUseMapper roomUseMapper;


    public Map<String,Object> getRoomTypeAnalysisCountData(Map<String, Object> paramMap){
        return roomUseMapper.getRoomTypeAnalysisReportCount(paramMap);
    }

    public Map<String,Object> getRoomListNotNull(Map<String, Object> roomList){
        //判空处理
        if (StringUtils.isEmpty((String)roomList.get("thisyearrentalNum"))){
            roomList.put("thisyearrentalNum",0);
        }
        if (StringUtils.isEmpty((String)roomList.get("roomrate"))){
            roomList.put("roomrate",0);
        }
        if (StringUtils.isEmpty((String)roomList.get("roomavgfee"))){
            roomList.put("roomavgfee",0);
        }
        if (StringUtils.isEmpty((String)roomList.get("rentalNum"))){
            roomList.put("rentalNum",0);
        }
        return roomList;
    }
}
