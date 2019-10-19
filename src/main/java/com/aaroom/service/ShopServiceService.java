package com.aaroom.service;

import com.aaroom.beans.ShopService;
import com.aaroom.persistence.ShopServiceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ShopServiceService {

    @Resource
    private ShopServiceMapper shopServiceMapper;

    public List<ShopService> selectByParams(Map<String,Object> params){
        return shopServiceMapper.selectByParams(params);
    }

    public int insertSelective(ShopService record){
        return shopServiceMapper.insertSelective(record);
    }

}
