package com.shangmei.persistence;

import com.shangmei.beans.ChkoutPay;

public interface ChkoutPayMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChkoutPay record);

    int insertSelective(ChkoutPay record);

    ChkoutPay selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChkoutPay record);

    int updateByPrimaryKeyWithBLOBs(ChkoutPay record);

    int updateByPrimaryKey(ChkoutPay record);
}