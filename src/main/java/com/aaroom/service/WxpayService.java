package com.aaroom.service;

import com.aaroom.model.OrderParam;
import com.aaroom.model.Price;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.impl.RedisCacheService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Constants;
import com.aaroom.wechat.Wechat;
import com.aaroom.wechat.bean.Order;
import com.aaroom.wechat.bean.WxOrder;
import com.aaroom.wechat.pay.MyWXpayConfig;
import com.aaroom.wechat.pay.WXPay;
import com.aaroom.wechat.pay.WXPayUtil;
import com.aaroom.wechat.persistence.MemberInfoMapper;
import com.aaroom.wechat.persistence.WxOrderMapper;
import com.aaroom.wechat.persistence.WxpayMapper;
import com.aaroom.wechat.service.WechatOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sosoda on 2019/2/21.
 */
@Service
public class WxpayService {


    @Autowired
    private MyWXpayConfig myWXpayConfig;

    @Autowired
    private Wechat wechat;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private WxpayMapper wxpayMapper;

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Autowired
    private WxOrderMapper wxOrderMapper;

    @Autowired
    private ShangmeiOTAService shangmeiOTAService;

    @Autowired
    private WechatOrderService wechatOrderService;


    public Map<String, String> weixinPay(String body, String orderId, long totalPricePay, String userIp, String openId, String goods_tag) {

        WXPay wxpay = null;
        try {
            wxpay = new WXPay(myWXpayConfig, wechat.getNotifyUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        data.put("out_trade_no", orderId);
        data.put("fee_type", "CNY");
        data.put("total_fee", totalPricePay + "");
        data.put("spbill_create_ip", userIp);
        data.put("notify_url", wechat.getNotifyUrl());
        data.put("trade_type", "JSAPI");  // 此处指定为扫码支付
        data.put("openid", openId);
        data.put("goods_tag", goods_tag);

        Map<String, String> resp = null;
        try {
            resp = wxpay.unifiedOrder(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String nonce = WXPayUtil.generateNonceStr();
        long s = new Date().getTime()/1000;
        String timeStamp = s+"";
        //再算签名
        String newPrepay_id = "prepay_id=" + resp.get("prepay_id");
        String args = "appId=" + myWXpayConfig.getAppID()
                + "&nonceStr=" + nonce
                + "&package=" + newPrepay_id
                + "&signType=MD5"
                + "&timeStamp=" + timeStamp
                + "&key=" + myWXpayConfig.getKey();
        String paySign = null;
        try {
            paySign = WXPayUtil.MD5(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,String> ret = new HashMap<>();
        ret.put("nonceStr",nonce);
        ret.put("paySign",paySign);
        ret.put("package",newPrepay_id);
        ret.put("timeStamp",timeStamp);
        ret.put("order_id",orderId);
        return ret;
    }

    public String notifyreceive(String result) {
        Map<String, String> map;
        String resXml = null;
        try {
            map = WXPayUtil.xmlToMap(result);
            WXPay wxPay = new WXPay(myWXpayConfig);
            if (map.size() != 0) {
                if (map.get("result_code").equalsIgnoreCase("SUCCESS")) {
                    if (wxPay.isResponseSignatureValid(map)) {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        redisCacheService.put(map.get("openid"), "success", 60);
                        wxpayMapper.updateOrderStatus(map.get("out_trade_no"));
                        memberInfoMapper.updateMemberLevel(map.get("openid"), "v2");
                        System.out.println("ResponseWXSuccess=====success");
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[FAILD]]></return_code>"
                                + "<return_msg><![CDATA[签名验证失败]]></return_msg>" + "</xml> ";
                        redisCacheService.put(map.get("openid"), "fail", 60);
                        System.out.println("ResponseWXFaild=====SignCheckFaild");
                    }
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAILD]]></return_code>"
                            + "<return_msg><![CDATA[返回值有误]]></return_msg>" + "</xml> ";
                    redisCacheService.put(map.get("openid"), "fail", 60);
                    System.out.println("ResponseWXFaild=====ResultFaild1");
                }
            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAILD]]></return_code>"
                        + "<return_msg><![CDATA[返回值有误]]></return_msg>" + "</xml> ";
                redisCacheService.put(map.get("openid"), "fail", 60);
                System.out.println("ResponseWXFaild=====ResultFaild2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resXml;
    }

    public void insertOrderData(WxOrder wxOrder) {
        wxpayMapper.insertOrderData(wxOrder);
    }

    public Map<String, Object> confirmResult(String openid, String orderId, String memberId) {
        Map<String, Object> map = new HashMap<>();
        String str;
        for (int i = 1; i <= 10; i++) {
            str = redisCacheService.get(openid);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ("success".equals(str)) {
                wxpayMapper.updateOrderStatus(orderId);
                memberInfoMapper.updateMemberLevel(memberId, "v2");
                map.put("status", "success");
                map.put("msg", "账单确认支付成功");
                return map;
            }
        }
        map.put("status", "fail");
        map.put("msg", "账单确认失败！");
        return map;
    }

    public Map<String,Object> confirmPayHotelFee(String openId, String orderId, String memberId,String order_no) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            String str = redisCacheService.get(openId);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ("success".equals(str)) {
                wxpayMapper.updateOrderStatus(orderId);
                //变更(订单状态/支付状态)
                wxOrderMapper.updateOrderPayStatus(memberId,order_no);
                map.put("status", "success");
                map.put("msg", "账单确认支付成功");
                return map;
            }
        }
        map.put("status", "fail");
        map.put("msg", "账单确认失败！");
        return map;
    }

    //微信退款
    public Map<String,String> refundHotelFee(String out_trade_no,String channel_order_no) {
        System.out.println("退款开始++++++++++++++++++++++++++++");
        //根据微信已支付订单号来获取订单金额
        Map<String,String> ret = new HashMap<>();
        //查看住房订单状态 入住不能退款
        Order order = wxOrderMapper.queryOrderByOrerNo(channel_order_no);
        if (order.getStatus().equals("已转入住") && order.getStatus().equals("部分转入住") ){
            ret.put("status","fail");
            ret.put("msg", "房间已转入住无法退款!");
            return ret;
        }
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(myWXpayConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //根据订单查询退款信息
        Double fee = wxOrderMapper.getTotalFee(out_trade_no,channel_order_no);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no",out_trade_no);
        data.put("out_refund_no","RENO"+CommonUtil.createUUID().toUpperCase());
        data.put("total_fee", String.format("%.0f",fee));
        data.put("refund_fee",String.format("%.0f",fee));
        Map<String, String> resp = null;
        try {
            resp = wxpay.refund(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("退款信息:"+resp);
        return resp;
    }

    public String notifyreceiveOrder(String result) {
        Map<String, String> map;
        String resXml = null;
        try {
            map = WXPayUtil.xmlToMap(result);
            WXPay wxPay = new WXPay(myWXpayConfig);
            if (map.size() != 0) {
                if (map.get("result_code").equalsIgnoreCase("SUCCESS")) {
                    if (wxPay.isResponseSignatureValid(map)) {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        //在微信订单查询住房订单信息
                        String out_trade_no = map.get("out_trade_no");
                        String openid = map.get("openid");
                        redisCacheService.put(openid, "success", 60);
                        String channel_order_no= wxpayMapper.queryWXorderByTradeNo(out_trade_no);
                        wxpayMapper.updateOrderStatus(out_trade_no);
                        //查询已有的参数信息
                        OrderParam param = wxpayMapper.getOrderInfo(channel_order_no);
                        param.setPay_status(1);
                        List<Price> priceList = wxOrderMapper.getPriceListByChannelOrderNo(channel_order_no);
                        param.setPrice_list(priceList);
                        ShangmeiRetView exchange = shangmeiOTAService.exchange(Constants.ORDER_SAVE, HttpMethod.POST, param, ShangmeiRetView.class);
                        System.out.println("exchange+++++++++++++++++++++++++++++++"+exchange);
                        if(exchange.getError_code() == 0){
                            //更新补全订单
                            wechatOrderService.updateOrderNumber(exchange.getEntity());
                        }else {
                            String error_msg = exchange.getError_msg();
                            resXml = "<xml>" + "<return_code><![CDATA[FAILD]]></return_code>"
                                    + "<return_msg><![CDATA[下单失败:"+error_msg+"]]></return_msg>" + "</xml> ";
                            redisCacheService.put(map.get("openid"), "fail", 60);
                            //退款
                            refundHotelFee(out_trade_no,channel_order_no);
                            System.out.println("ResponseWXFaild=====PlaceOrderFaild");
                        }
                        System.out.println("ResponseWXSuccess=====success");
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[FAILD]]></return_code>"
                                + "<return_msg><![CDATA[签名验证失败]]></return_msg>" + "</xml> ";
                        redisCacheService.put(map.get("openid"), "fail", 60);
                        System.out.println("ResponseWXFaild=====SignCheckFaild");
                    }
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAILD]]></return_code>"
                            + "<return_msg><![CDATA[返回值有误]]></return_msg>" + "</xml> ";
                    redisCacheService.put(map.get("openid"), "fail", 60);
                    System.out.println("ResponseWXFaild=====ResultFaild1");
                }
            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAILD]]></return_code>"
                        + "<return_msg><![CDATA[返回值有误]]></return_msg>" + "</xml> ";
                redisCacheService.put(map.get("openid"), "fail", 60);
                System.out.println("ResponseWXFaild=====ResultFaild2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resXml;
    }

    public Map<String, String> orderWeixinPay(String body, String orderId, long totalPricePay, String userIp, String openId, String goods_tag) {

        WXPay wxpay = null;
        try {
            wxpay = new WXPay(myWXpayConfig, wechat.getNotifyUrlOrder());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        data.put("out_trade_no", orderId);
        data.put("fee_type", "CNY");
        data.put("total_fee", totalPricePay + "");
        data.put("spbill_create_ip", userIp);
        data.put("notify_url", wechat.getNotifyUrlOrder());
        data.put("trade_type", "JSAPI");  // 此处指定为扫码支付
        data.put("openid", openId);
        data.put("goods_tag", goods_tag);

        Map<String, String> resp = null;
        try {
            resp = wxpay.unifiedOrder(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String nonce = WXPayUtil.generateNonceStr();
        long s = new Date().getTime()/1000;
        String timeStamp = s+"";
        //再算签名
        String newPrepay_id = "prepay_id=" + resp.get("prepay_id");
        String args = "appId=" + myWXpayConfig.getAppID()
                + "&nonceStr=" + nonce
                + "&package=" + newPrepay_id
                + "&signType=MD5"
                + "&timeStamp=" + timeStamp
                + "&key=" + myWXpayConfig.getKey();
        String paySign = null;
        try {
            paySign = WXPayUtil.MD5(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,String> ret = new HashMap<>();
        ret.put("nonceStr",nonce);
        ret.put("paySign",paySign);
        ret.put("package",newPrepay_id);
        ret.put("timeStamp",timeStamp);
        ret.put("order_id",orderId);
        return ret;
    }
}
