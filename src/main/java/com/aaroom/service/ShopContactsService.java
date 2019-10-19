package com.aaroom.service;

import com.aaroom.beans.ShopContacts;
import com.aaroom.persistence.ShopContactsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ShopContactsService {

    @Resource
    private ShopContactsMapper shopContactsMapper;


    public List<ShopContacts> selectByParams(Map<String,Object> params){
        return shopContactsMapper.getShopContactsList(params);
    }
}
