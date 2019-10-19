package com.aaroom.wechat.service;

import com.aaroom.wechat.bean.Configuration;
import com.aaroom.wechat.persistence.WechatLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-07 14:48
 **/
@Service
public class WechatLoginService {

    @Autowired
    private WechatLoginMapper wechatLoginMapper;

    public Configuration getConfigurationByName(String wxOrderPrice) {
        return wechatLoginMapper.getConfigurationByName(wxOrderPrice);
    }
}
