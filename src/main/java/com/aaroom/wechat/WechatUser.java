package com.aaroom.wechat;

import lombok.Data;

@Data
public class WechatUser {

    private String openid;
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String[] privilege;
    private String unionid;
    
    private int edition;

    // 错误时出现
    private int errcode;
    private String errmsg;

}
