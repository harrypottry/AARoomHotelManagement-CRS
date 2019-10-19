package com.aaroom.wechat.persistence;

import com.aaroom.wechat.bean.MemberCollection;

import java.util.List;
import java.util.Map;

public interface MemberCollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberCollection record);

    int insertSelective(MemberCollection record);

    MemberCollection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberCollection record);

    int updateByPrimaryKey(MemberCollection record);

    List<MemberCollection> getMemberCollectionByMemberId(String member_id);

    List<MemberCollection> getMemberCollectionByEntity(Map<String,String> params);

    int deleteByEntity(MemberCollection record);

}