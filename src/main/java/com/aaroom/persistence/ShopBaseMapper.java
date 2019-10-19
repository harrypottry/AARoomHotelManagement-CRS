package com.aaroom.persistence;

import com.aaroom.beans.HotelView;
import com.aaroom.beans.ShopBase;

import java.util.List;
import java.util.Map;

public interface ShopBaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopBase record);

    int insertSelective(ShopBase record);

    ShopBase selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopBase record);

    int updateByPrimaryKey(ShopBase record);

    List<ShopBase> selectShopBaseByParams(Map<String,Object> params);


    ShopBase selectByParams(Map<String,Object> params);

}