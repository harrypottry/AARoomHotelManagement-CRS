package com.aaroom.wechat.persistence;

import com.aaroom.wechat.bean.MemberComment;
import java.util.List;
import java.util.Map;

public interface MemberCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberComment record);

    int insertSelective(MemberComment record);

    MemberComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Map params);

    int updateByPrimaryKey(MemberComment record);

    List<MemberComment> getMemberCommentByParams(Map<String,Object> params);

    List<Map<String,Object>> getCommentListByParams(Map<String,Object> params);
}