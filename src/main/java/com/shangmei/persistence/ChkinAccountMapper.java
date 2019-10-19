package com.shangmei.persistence;

import com.shangmei.beans.ChkinAccount;

import java.util.List;
import java.util.Map;

public interface ChkinAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChkinAccount record);

    int insertSelective(ChkinAccount record);

    ChkinAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChkinAccount record);

    int updateByPrimaryKeyWithBLOBs(ChkinAccount record);

    int updateByPrimaryKey(ChkinAccount record);

    List<Map<String, Object>> getChkOutRefund(Map<String, Object> paramMap);
}