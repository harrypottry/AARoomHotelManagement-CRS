package com.aaroom.wechat.bean.model;

import lombok.Data;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-11 13:40
 **/
@Data
public class WechatLoginParams {

    private String encryptedData;

    private String iv;

    private String code;
}
