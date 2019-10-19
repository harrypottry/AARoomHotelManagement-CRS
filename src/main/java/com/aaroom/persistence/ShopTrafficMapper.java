package com.aaroom.persistence;

import com.aaroom.beans.ShopTraffic;

import java.util.List;
import java.util.Map;

public interface ShopTrafficMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByShopId(String shop_id);

    int insert(ShopTraffic record);

    int insertSelective(ShopTraffic record);

    ShopTraffic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopTraffic record);

    int updateByPrimaryKey(ShopTraffic record);

    List<ShopTraffic> selectByParams(Map<String,Object> params);
}