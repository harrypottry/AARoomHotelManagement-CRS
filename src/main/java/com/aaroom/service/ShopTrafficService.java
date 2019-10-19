package com.aaroom.service;

import com.aaroom.beans.ShopTraffic;
import com.aaroom.persistence.ShopTrafficMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ShopTrafficService {

    @Resource
    private ShopTrafficMapper shopTrafficMapper;

    public List<ShopTraffic> selectByParams(Map<String,Object> params){
        return shopTrafficMapper.selectByParams(params);
    }
}
