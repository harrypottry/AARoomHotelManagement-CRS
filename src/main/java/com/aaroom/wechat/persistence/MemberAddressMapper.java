package com.aaroom.wechat.persistence;

import com.aaroom.wechat.bean.MemberAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberAddress record);

    int insertSelective(MemberAddress record);

    MemberAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberAddress record);

    int updateByPrimaryKey(MemberAddress record);

    List<MemberAddress> getAllMemberAddress(String member);
}