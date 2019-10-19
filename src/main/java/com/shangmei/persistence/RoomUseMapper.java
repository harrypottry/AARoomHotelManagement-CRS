package com.shangmei.persistence;

import com.shangmei.beans.RoomUse;

import java.util.Map;

public interface RoomUseMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoomUse record);

    int insertSelective(RoomUse record);

    RoomUse selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RoomUse record);

    int updateByPrimaryKey(RoomUse record);

    Map<String,Object> getRoomTypeAnalysisReportCount(Map<String, Object> paramMap);
}