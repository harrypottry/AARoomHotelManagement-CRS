package com.aaroom.wechat.service;

import com.aaroom.wechat.bean.MemberInvoice;
import com.aaroom.wechat.persistence.MemberInvoiceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class MemberInvoiceService {

    @Resource
    private MemberInvoiceMapper memberInvoiceMapper;

    public int insertMemberInvoice(MemberInvoice record){
        return memberInvoiceMapper.insertSelective(record);
    }

    public List<MemberInvoice> getAllMemberInvoice(String member_id){
        return memberInvoiceMapper.getAllMemberInvoice(member_id);
    }

    public MemberInvoice selectByPrimaryKey(Integer id){
        return memberInvoiceMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(MemberInvoice record){
        return memberInvoiceMapper.updateByPrimaryKeySelective(record);
    }

    public int deleteByPrimaryKey(Integer id){
        return memberInvoiceMapper.deleteByPrimaryKey(id);
    }

    public List<Map<String,Object>> getMemberInvoiceListByParams(Map<String,Object> params){
        return memberInvoiceMapper.getMemberInvoiceListByParams(params);
    }

    public int updateByPrimaryKeyParams(Map params){
        return memberInvoiceMapper.updateByPrimaryKeyParams(params);
    }
}
