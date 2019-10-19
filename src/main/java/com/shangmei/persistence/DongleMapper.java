package com.shangmei.persistence;

import com.shangmei.beans.Dongle;

public interface DongleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Dongle record);

    int insertSelective(Dongle record);

    Dongle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Dongle record);

    int updateByPrimaryKeyWithBLOBs(Dongle record);

    int updateByPrimaryKey(Dongle record);
}