package com.aaroom.wechat.service;

import com.aaroom.wechat.bean.MemberComment;
import com.aaroom.wechat.persistence.MemberCommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class MemberCommentService {

    @Resource
    private MemberCommentMapper memberCommentMapper;

    public int insertSelective(MemberComment record){
        return memberCommentMapper.insertSelective(record);
    }

    public int deleteByPrimaryKey(Integer id){
        return memberCommentMapper.deleteByPrimaryKey(id);
    }

    public List<MemberComment> getMemberCommentByParams(Map<String,Object> params){
        return memberCommentMapper.getMemberCommentByParams(params);
    }


    public MemberComment selectByPrimaryKey(Integer id){
        return memberCommentMapper.selectByPrimaryKey(id);
    }

    public List<Map<String,Object>> getCommentListByParams(Map<String,Object> params){
        return memberCommentMapper.getCommentListByParams(params);
    }

    public String getCommentByUtility(String utility){
        String str ;
        switch (utility){
            case "1" :
                str = "差评";
                break;
            case "2": case "3" :
                str = "中评";
                break;
            default:
                str = "好评";
                break;
        }
        return str;
    }

    public int updateByPrimaryKeySelective(Map params){
        return memberCommentMapper.updateByPrimaryKeySelective(params);
    }
}
