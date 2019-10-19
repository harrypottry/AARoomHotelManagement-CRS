package com.aaroom.persistence;

import com.aaroom.beans.ShopService;

import java.util.List;
import java.util.Map;

public interface ShopServiceMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByShopId(String shop_id);

    int insert(ShopService record);

    int insertSelective(ShopService record);

    ShopService selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopService record);

    int updateByPrimaryKey(ShopService record);

    List<ShopService> selectByParams(Map<String,Object> params);
}