package com.aaroom.wechat;


import com.aaroom.service.impl.RedisCacheService;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Wechat {

    private static final Logger LOG = LoggerFactory.getLogger(Wechat.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisCacheService redisCacheService;

    public static final String SleepingBeauty = "sb";

    public static final String HotelManagement = "hm";

    public static final String Client = "cl";

    @Setter
    private String mpToken;

    @Value("${weixin.client.appId}")
    private String appId;

    @Value("${weixin.client.appKey}")
    private String appKey;

    @Value("${weixin.hotelManagement.appid}")
    private String appid;

    @Value("${weixin.hotelManagement.secret}")
    private String secret;

    @Value("${weixin.hotelManagement.grant_type}")
    private String grant_type;

    /**
     * 获取Token接口
     */
    private final String tokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 小程序token获取接口
     */
    private final String tinyServerTokenUrl= "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    /**
     * 小程序token获取接口
     */
    private final String code2TokenUrl= "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    /**
     * 授权接口
     */

    private final String baseAuthUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";


    private final String userAuthUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect";


    @Getter
    private final String notifyUrl = "https://aaproducttest.bjlrsd.com/wxpay/notifyreceive";

    @Getter
    private final String notifyUrlOrder = "https://aaproducttest.bjlrsd.com/wxpay/notifyreceiveOrder";

    /**
     * 获取用户信息接口
     */
    @Setter
    private String userUrl;

    @Setter
    private String jsTokenUrl;

    @Setter
    private String jsTicketUrl;

    private static final String JS_TICKET = "WX_TICKET";

    // --------------

    public boolean check(String signature, long timestamp, String nonce) {
        String t = signature(timestamp, nonce);
        return signature.equals(t);
    }

    /**
     * 微信公众号接口： 根据code换取公众号Token。 与开发者账号不同
     * 
     * @param code
     * @return
     */
    public WechatToken mediaToken(String code, String appName) {
            return tinyToken(code, appId, appKey);
    }

    /**
     * 移动应用接口：根据code换取微信app Token
     * 
     * @param code
     * @return
     */
    public WechatToken appToken(String code, int edition) {
        String url = String.format(tokenUrl, appid, secret,code);//format替换url里的占位符
        String TokenString = restTemplate.getForObject(url, String.class);//请求外部链接地址 获得token
        WechatToken token = JSON.parseObject(TokenString.trim(), WechatToken.class);//去空格后 返回WechatToken对象
        token.setEdition(edition);//对象存个版本属性
        return token;
    }

    /**
     * 通过移动应用code获取用户信息
     * 
     * @param code
     * @return
     */
    /*public WechatUser getUser(WechatToken t) {
        if (t != null && t.getAccess_token() != null && t.getOpenid() != null) {
            WechatUser u = user(t.getAccess_token(), t.getOpenid());
            u.setEdition(t.getEdition());
            return u;
        }
        return null;
    }*/

    /**
     * 通过access_token获取用户信息， 目前主要是为了获取用户信息
     * 
     * @param access_token
     * @param openid
     * @return
     */
    public WechatUser user(String access_token, String openid) {
        String url = String.format(userUrl, access_token, openid);
        String s = restTemplate.getForObject(url, String.class);
        WechatUser user = JSON.parseObject(s.trim(), WechatUser.class);
        return user;
    }

    /**
     * 获取token。 因为公众号、 App、WEB的app和密钥不一样， 所以这里进行一下封装
     * 
     * @param code
     * @param app
     * @param key
     * @return
     */
    private WechatToken token(String code, String app, String key) {
        String url = String.format(tokenUrl, app, key, code);
        String s = restTemplate.getForObject(url, String.class);
        WechatToken token = JSON.parseObject(s.trim(), WechatToken.class);
        return token;
    }
    /**
     * 获取token。 因为公众号、 App、WEB的app和密钥不一样， 所以这里进行一下封装
     *
     * @param code
     * @param app
     * @param key
     * @return
     */
    private WechatToken tinyToken(String code, String app, String key) {
        String url = String.format(code2TokenUrl, app, key, code);
        String s = restTemplate.getForObject(url, String.class);
        WechatToken token = JSON.parseObject(s.trim(), WechatToken.class);
        return token;
    }

    /**
     * 签名算法
     * 
     * @param timestamp
     * @param nonce
     * @return
     */
    private String signature(long timestamp, String nonce) {

        List<String> data = new ArrayList<>();
        data.add(mpToken);
        data.add(String.valueOf(timestamp));
        data.add(nonce);

        Collections.sort(data);
        String str = data.get(0) + data.get(1) + data.get(2);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(str.getBytes("UTF8"));
            String t = new String(Hex.encodeHex(md.digest()));
            return t;
        } catch (Exception e) {
        }

        return null;
    }

    public String getTicket() {

        String ticket = redisCacheService.get(JS_TICKET, String.class);
        if (ticket == null) {
            WechatToken token = null;
            {
                String url = String.format(jsTokenUrl, appId, appKey);
                try {
                    String s = restTemplate.getForObject(url, String.class);
                    token = JSON.parseObject(s.trim(), WechatToken.class);
                } catch (Exception e) {
                    if (LOG.isWarnEnabled())
                        LOG.warn("request wechat js ticket error");
                    return "";
                }
                if (token.getErrcode() != 0 || StringUtils.isBlank(token.getAccess_token()))
                    return "";
            }

            {
                String url = String.format(jsTicketUrl, token.getAccess_token());
                String s = restTemplate.getForObject(url, String.class);
                WechatTicket wt = JSON.parseObject(s.trim(), WechatTicket.class);
                if (wt.getErrcode() != 0 || StringUtils.isBlank(wt.getTicket()))
                    return "";
                ticket = wt.getTicket();
                redisCacheService.put(JS_TICKET, ticket, 7000);
            }

        }

        return ticket;
    }



}
