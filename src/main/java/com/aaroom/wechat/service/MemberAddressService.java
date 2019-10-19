package com.aaroom.wechat.service;

import com.aaroom.wechat.bean.MemberAddress;
import com.aaroom.wechat.persistence.MemberAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MemberAddressService {

    @Autowired
    private MemberAddressMapper memberAddressMapper;

    public int insertMemberAddress(MemberAddress record){
        return memberAddressMapper.insertSelective(record);
    }

    public List<MemberAddress> getAllMemberAddress(String member){
        return memberAddressMapper.getAllMemberAddress(member);
    }

    public int updateByPrimaryKeySelective(MemberAddress record){
        return memberAddressMapper.updateByPrimaryKeySelective(record);
    }

    public MemberAddress selectByPrimaryKey(Integer id){
        return memberAddressMapper.selectByPrimaryKey(id);
    }

    public int deleteByPrimaryKey(Integer id){
        return memberAddressMapper.deleteByPrimaryKey(id);
    }
}
