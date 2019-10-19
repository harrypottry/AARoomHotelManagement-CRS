package com.aaroom.wechat.persistence;

import com.aaroom.wechat.bean.MemberPassenger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberPassengerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberPassenger record);

    int insertSelective(MemberPassenger record);

    MemberPassenger selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberPassenger record);

    int updateByPrimaryKey(MemberPassenger record);

    List<MemberPassenger> getAllMemberPassenger(String member_id);
}