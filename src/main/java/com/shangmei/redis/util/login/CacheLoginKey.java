package com.shangmei.redis.util.login;

import com.shangmei.redis.util.RedisCommKey;

/***
 * 登录
 * 
 * @author shangMei
 *
 */
public class CacheLoginKey {
    /**********当前登录的数据信息**************/
    private static final String CURRENT_LOGIN_SHOP = "LOGIN::SHOP";//当前登录的门店
    
    /**********登录过的员工数据信息**************/
    private static final String LOGIN_STAFFID_STR = "LOGIN::STAFF";//登陆过系统的员工
    
    /**
     * 获得当前登录的门店记录
     * 
     * @return
     */
    public static String getCurrentLoginShopKey() {
        return new StringBuffer(RedisCommKey.getKeyProject()).append(RedisCommKey.getKeyJoinTag()).append(CURRENT_LOGIN_SHOP).toString() ;
    }
    
    /**
     * 获取登陆过系统的员工信息
     * 
     * @return
     */
    public static String getLoginStaffStr(){
        return new StringBuffer(RedisCommKey.getKeyProject()).append(RedisCommKey.getKeyJoinTag()).append( LOGIN_STAFFID_STR).toString() ;
    }
}
