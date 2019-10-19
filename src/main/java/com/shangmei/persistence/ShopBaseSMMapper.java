package com.shangmei.persistence;

import com.shangmei.beans.ShopBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopBaseSMMapper {
    int deleteByPrimaryKey(String id);

    int insert(ShopBase record);

    int insertSelective(ShopBase record);

    ShopBase selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ShopBase record);

    int updateByPrimaryKey(ShopBase record);

    List<ShopBase> getShopBaseSMByAABrandId();

    ShopBase getDataByHotelId(@Param(value = "shop_id") String shop_id);
}