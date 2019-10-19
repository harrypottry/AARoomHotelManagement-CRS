package com.aaroom.wechat.controller;

import com.aaroom.wechat.MessageCodec;
import com.aaroom.wechat.bean.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@RestController
public class WechatController {

    private static final Logger LOG = LoggerFactory.getLogger(WechatController.class);

    @Autowired
    private com.aaroom.wechat.Wechat wechat;


    private MessageCodec coder = new MessageCodec();

    // /wechat/api?signature=2fba2e57bf47ed0e617b5d3ade13f1680b0d257c&echostr=2565823351778964215&timestamp=1409732706&nonce=597809717

    /**
     * 用于提供给微信校验签名。 如果校验通过，则输出echostr
     * 
     * @param request
     * @param response
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @throws IOException
     */
    @RequestMapping(value = "/wechat/api", method = RequestMethod.GET)
    public void check(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "signature") String signature,
            @RequestParam(value = "timestamp") long timestamp,
            @RequestParam(value = "nonce") String nonce,
            @RequestParam(value = "echostr") String echostr) throws IOException {
        // 如果是首次绑定
        if (wechat.check(signature, timestamp, nonce)) {
            Writer out = response.getWriter();
            out.write(echostr);
            out.flush();
            return;
        }
    }

    /**
     * 处理微信过来的消息, 暂时没有使用
     * 
     * @param request
     * @param response
     * @param httpEntity
     * @param signature
     * @param timestamp
     * @param nonce
     * @throws IOException
     */
    @RequestMapping(value = "/wechat/api", method = RequestMethod.POST)
    public void wechatApi(HttpServletRequest request,
            HttpServletResponse response,
            HttpEntity<String> httpEntity,
            @RequestParam(value = "signature") String signature,
            @RequestParam(value = "timestamp") long timestamp,
            @RequestParam(value = "nonce") String nonce) throws IOException {
        // 如果是首次绑定
        if (!wechat.check(signature, timestamp, nonce))
            ;// return "";

        String body = httpEntity.getBody();
        Message message = coder.parse(body);

        // TODO:

        // 处理消息, 并生成响应
        Message resp = null;
        String xml = coder.toXml(resp);
        Writer out = response.getWriter();
        out.write(xml);
        out.flush();
    }

}
