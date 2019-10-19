package com.aaroom.persistence;

import com.aaroom.beans.ShopPicture;

import java.util.List;
import java.util.Map;

public interface ShopPictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopPicture record);

    int insertSelective(ShopPicture record);

    List<ShopPicture> selectByShopPicturePrimaryKey(Map map);

    int updateByPrimaryKeySelective(Map<String,Object> map);

    int updateByPrimaryKey(ShopPicture record);
}