package com.aaroom.wechat.persistence;

import com.aaroom.wechat.bean.MemberInvoice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MemberInvoiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberInvoice record);

    int insertSelective(MemberInvoice record);

    MemberInvoice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberInvoice record);

    int updateByPrimaryKey(MemberInvoice record);

    int updateByPrimaryKeyParams(Map params);

    List<MemberInvoice> getAllMemberInvoice(String member_id);

    List<Map<String,Object>> getMemberInvoiceListByParams(Map<String,Object> params);
}