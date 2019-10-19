package com.aaroom.wechat;

import lombok.Data;

@Data
public class WechatToken {

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String session_key;

    /**
     * 标识投资版或融资版
     */
    private int edition;

    // 错误时出现
    private int errcode;
    private String errmsg;

}
