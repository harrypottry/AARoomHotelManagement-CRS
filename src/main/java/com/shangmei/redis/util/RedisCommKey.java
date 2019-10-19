package com.shangmei.redis.util;

/**
 * redis key值公共参数
 * 
 * @author ethank
 *
 */
public class RedisCommKey {
    /**
     * key值之间的分隔符
     */
    private static final String KEY_JOIN_TAG="::";
    /**
     * 所有key值的前缀
     */
    private static final String KEY_PROJECT="pms";
    
    /***
     * 获取所有key知道额前缀
     * 
     * @return
     */
    public static String getKeyProject(){
        return KEY_PROJECT;
    }
    
    /***
     * 获取key之间的连接符
     * 
     * @return
     */
    public static String getKeyJoinTag(){
        return KEY_JOIN_TAG;
    }
}
