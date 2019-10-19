package com.aaroom.wechat.controller;

import com.aaroom.service.WxpayService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.wechat.bean.Configuration;
import com.aaroom.wechat.bean.Member;
import com.aaroom.wechat.bean.WxOrder;
import com.aaroom.wechat.service.MemberInfoService;
import com.aaroom.wechat.service.WechatLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-12 14:43
 **/
@RestController
public class WechatPayController {

    @Autowired
    private WxpayService wxpayService;

    @Autowired
    private WechatLoginService wechatLoginService;

    @Autowired
    private MemberInfoService memberInfoService;

    //微信支付获取预支付ID
    @GetMapping(value = "/WeChat/console/api/getWechatPayPreId")
    public Map<String, String> getWechatPayPreId(HttpServletRequest request, @RequestParam String memberId) {
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        Member member = memberInfoService.getMemberById(memberId);
        Configuration configuration = wechatLoginService.getConfigurationByName("wxOrderPrice");
        member.setPromoteDate(new Date());
        member.setPromotePrice(Double.valueOf(configuration.getData()));
        memberInfoService.updateBuyVipInfo(member);
        WxOrder wxOrder = new WxOrder();
        wxOrder.setId(CommonUtil.createUUID());
        String body = "会员升级";
        wxOrder.setBody(body);
        wxOrder.setFeeType("CNY");
        String uuid = CommonUtil.createUUID();
        uuid = uuid.substring(0,18);
        String orderId = new Date().getTime() + "" + uuid;
        wxOrder.setOutTradeNo(orderId);
        wxOrder.setGoodsTag("");
        wxOrder.setOpenId(member.getOpenId());
        wxOrder.setSpbillCreateIp(ip);
        wxOrder.setStatus(0);
        wxOrder.setTotalFee(configuration.getData());
        wxOrder.setTradeType("JSAPI");
        wxpayService.insertOrderData(wxOrder);
        Map<String, String> map = wxpayService.weixinPay(body, orderId ,Long.valueOf(configuration.getData()), ip,member.getOpenId(), "");
        return map;
    }

    //回调地址
    @PostMapping(value = "/wxpay/notifyreceive")
    public String notifyreceive(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("===============微信请求进入回调函数================");
        InputStream inStream;
        String str = null;
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
            System.out.println("WXResult====="+result);
            str = wxpayService.notifyreceive(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    //确认实际支付结果
    @GetMapping(value = "/WeChat/console/api/confirmResult")
    public Map<String, Object> confirmResult(@RequestParam String memberId, @RequestParam String orderId) {
        Member member = memberInfoService.getMemberById(memberId);
        return wxpayService.confirmResult(member.getOpenId(), orderId, memberId);
    }
}
