package com.aaroom.wechat.service;

import com.aaroom.wechat.bean.MemberPassenger;
import com.aaroom.wechat.persistence.MemberPassengerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberPassengerService {

    @Resource
    private MemberPassengerMapper memberPassengerMapper;

    public int insertMemberPassenger(MemberPassenger record){
        return memberPassengerMapper.insertSelective(record);
    }

    public List<MemberPassenger> getAllMemberPassenger(String member_id){
        return memberPassengerMapper.getAllMemberPassenger(member_id);
    }

    public MemberPassenger selectByPrimaryKey(Integer id){
        return memberPassengerMapper.selectByPrimaryKey(id);
    }

    public int deleteByPrimaryKey(Integer id){
        return memberPassengerMapper.deleteByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(MemberPassenger record){
        return memberPassengerMapper.updateByPrimaryKeySelective(record);
    }
}
