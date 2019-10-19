package com.aaroom.wechat.persistence;

import com.aaroom.wechat.bean.Configuration;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WechatLoginMapper {

    Configuration getConfigurationByName(@Param(value = "wxOrderPrice")String wxOrderPrice);
}
