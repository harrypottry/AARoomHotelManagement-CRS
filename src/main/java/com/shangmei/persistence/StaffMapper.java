package com.shangmei.persistence;

import com.shangmei.beans.Staff;

import java.util.Map;

public interface StaffMapper {
    int deleteByPrimaryKey(String id);

    int insert(Staff record);

    int insertSelective(Staff record);

    Staff selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Staff record);

    int updateByPrimaryKey(Staff record);

    String getIsDongle(Map<String, Object> map);

    Map<String, Object> selectStaffForLogin(Map<String, Object> map);
}