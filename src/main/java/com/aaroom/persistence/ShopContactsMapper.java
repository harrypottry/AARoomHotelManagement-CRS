package com.aaroom.persistence;

import com.aaroom.beans.ShopContacts;

import java.util.List;
import java.util.Map;

public interface ShopContactsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShopContacts record);

    int insertSelective(ShopContacts record);

    ShopContacts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopContacts record);

    int updateByPrimaryKey(ShopContacts record);

    List<ShopContacts> getShopContactsList(Map<String,Object> params);
}