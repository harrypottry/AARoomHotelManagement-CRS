package com.aaroom.wechat;

import lombok.Data;

@Data
public class WechatTicket {

    private String ticket;
    private int expires_in;

    // 错误时出现
    private int errcode;
    private String errmsg;

}
