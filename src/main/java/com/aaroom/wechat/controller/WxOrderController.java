package com.aaroom.wechat.controller;

import com.aaroom.exception.RestError;
import com.aaroom.model.OrderParam;
import com.aaroom.model.Price;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.ShangmeiOTAService;
import com.aaroom.service.WxpayService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Constants;
import com.aaroom.wechat.bean.Invoice;
import com.aaroom.wechat.bean.Member;
import com.aaroom.wechat.bean.Order;
import com.aaroom.wechat.bean.WxOrder;
import com.aaroom.wechat.bean.model.MemberInfoView;
import com.aaroom.wechat.service.MemberInfoService;
import com.aaroom.wechat.service.WechatOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyp on 2019/3/11 0011.
 */
@RestController
public class WxOrderController {
    @Autowired
    private ShangmeiOTAService shangmeiOTAService;

    @Autowired
    private WxpayService wxpayService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Autowired
    private WechatOrderService wechatOrderService;


    //查询对应时间内的房量以及价格
    @PostMapping("/WeChat/console/api/avail")
    public ShangmeiRetView hotel(@RequestParam String hotel_id,
                        @RequestParam String beginDate,
                        @RequestParam String endDate){
        Map<String, String> params = new HashMap<>();
        params.put("hotel_code",hotel_id);
        params.put("start",beginDate);
        params.put("end",endDate);
        ShangmeiRetView exchange = shangmeiOTAService.exchange(Constants.AVAIL, HttpMethod.POST, params, ShangmeiRetView.class);
        if (exchange.getError_code() == 0) {
            //计算三挡房价价格
            Map<String, Object> entityData = wechatOrderService.countRoomTypePrice(exchange.getEntity());
            exchange.setEntity(entityData);
        }
        return exchange;
    }
    //下单接口
    @PostMapping("/WeChat/console/api/orderSave")
    public Object order(@RequestBody OrderParam params){
        params.setChannel_order_no(CommonUtil.createUUID());
        ShangmeiRetView<Object> result= new ShangmeiRetView<>();
        //查会员等级
        MemberInfoView member = memberInfoService.getMemberInfo(params.getMemberId());
        if (member.getLevelNum().equals("v1")){
            params.setPriceCode("Wprc");
        } else if(member.getLevelNum().equals("v2")){
            params.setPriceCode("Yprc");
        } else if(member.getLevelNum().equals("v3")){
            params.setPriceCode("Gprc");
        }
        String flag = wechatOrderService.insertOrder(params);
        if (flag != null){
            result.setEntity(flag);
            result.setError_code(0);
            result.setError_msg("新增成功");
            return result;
        }else {
            result.setEntity("");
            result.setError_code(401);
            result.setError_msg("新增失败");
            return result;
        }
    }
    //查询订单详情
    @PostMapping("/WeChat/console/api/selectOrder")
    public ShangmeiRetView selectOrder(@RequestParam String channel_order_no){
        Map<String, String> params = new HashMap<>();
        params.put("channel_order_no",channel_order_no);
        //查订单状态
        ShangmeiRetView exchange = new ShangmeiRetView();
        Integer payStatus = wechatOrderService.getOrderPayStatusByChannelOrderNo(channel_order_no);
        if (payStatus == 1){
            OrderParam prePayOrderInfo = wechatOrderService.getPrePayOrderInfo(channel_order_no);
            exchange.setEntity(prePayOrderInfo);
            exchange.setError_msg("success");
            exchange.setError_debug("");
            exchange.setError_code(0);
            return exchange;
        }
        String order_no = wechatOrderService.getOrderNoByChannelOrderNo(channel_order_no);
        params.put("order_no",order_no);
        exchange = shangmeiOTAService.exchange(Constants.ORDER_QUERY, HttpMethod.POST, params, ShangmeiRetView.class);
        //根据会员等级计算房价价格
        if(exchange.getError_code() == 0){
            Map<String, Object> entityData = wechatOrderService.queryRoomTypePrice(exchange.getEntity(),order_no);
            exchange.setEntity(entityData);
        }
        return exchange;
    }
    //取消订单
    @PostMapping("/WeChat/console/api/cancelOrder")
    public ShangmeiRetView cancelOrder(@RequestParam String hotel_id,
                                       @RequestParam String channel_order_no,
                                       @RequestParam (required = false)String remark){
        ShangmeiRetView<Object> result= new ShangmeiRetView<>();
        //判断当前时间是否允许退房
        String arrival_time = wechatOrderService.getArrivalTime(channel_order_no);
        String arrivalDate = arrival_time.substring(0,10);
        String arrivalTime = arrivalDate+" 18:00:00";
        Date date = CommonUtil.strToDate(arrivalTime, "yyyy-MM-dd HH:mm:ss");
        if(new Date().after(date)){
            result.setEntity("");
            result.setError_code(303);
            result.setError_debug("cancel time late");
            result.setError_msg("只能在入住当天六点前退房");
            return result;
        }
        Map<String, String> params = new HashMap<>();
            //订单的支付状态
        Integer payStatus = wechatOrderService.getOrderPayStatusByChannelOrderNo(channel_order_no);
            if (payStatus == 1){
                //预订单改aa_order状态
                wechatOrderService.updateOrderStatusByChannelOrderNo(channel_order_no);
                result.setEntity(channel_order_no);
                result.setError_code(0);
                result.setError_debug("");
                result.setError_msg("success");
                return result;
            }
        //根据channel_order_no获取order_no; 付款成功的单号
        String order_no = wechatOrderService.getOrderNoByChannelOrderNo(channel_order_no);
        params.put("order_no",order_no);
        params.put("channel_order_no",channel_order_no);
        //判断订单状态是否已转入住
        ShangmeiRetView ret = shangmeiOTAService.exchange(Constants.ORDER_QUERY, HttpMethod.POST, params, ShangmeiRetView.class);
        if(wechatOrderService.isCheckIn(ret)){
            result.setEntity("");
            result.setError_code(301);
            result.setError_debug("is check in");
            result.setError_msg("订单已转入住或部分转入住");
            return result;
        }else if(wechatOrderService.isCencel(ret)){
            result.setEntity("");
            result.setError_code(302);
            result.setError_debug("order is cancel");
            result.setError_msg("订单已取消");
            return result;
        }
        params.put("hotel_code",hotel_id);
        params.put("remark",remark);
        //调用尚美api取消订单
        ShangmeiRetView exchange = shangmeiOTAService.exchange(Constants.ORDER_CANCEL, HttpMethod.POST, params, ShangmeiRetView.class);
        //订单取消成功  更新aa数据库状态
        if(exchange.getError_code()==0){
            wechatOrderService.updateOrderStatus(params);
            //根据订单id查询out_trade_no
            String out_trade_no = wechatOrderService.getOutTradeNo(channel_order_no);
            //查看是否成功支付
            String pay_status = wechatOrderService.getOrderPayStatus(out_trade_no);
            if(pay_status != null){
                if (pay_status.equals("1")){
                    wxpayService.refundHotelFee(out_trade_no,channel_order_no);
                    //更新订单状态  将已支付状态变为已退款
                    wechatOrderService.updateWXOrderPayStatus(out_trade_no);
                }
            }
        }
        return exchange;
    }
    //查看全部订单
    @PostMapping("/WeChat/console/api/queryOrderList")
    public RestError queryOrderList(@RequestParam String memberId,
                                    @RequestParam (required = false) Integer pay_status,
                                    @RequestParam (required = false) Integer comment_status){
        List<Order> orderList= wechatOrderService.queryOrderList(memberId,comment_status,pay_status);
        return new RestError(orderList);
    }
    //微信支付房费
    @PostMapping("/WeChat/console/api/payHotelFee")
    public Map<String, String> payHotelFee(HttpServletRequest request,
                                           @RequestParam String memberId,
                                           @RequestParam String channel_order_no){
        String ip = request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for");
        //获取会员的基本信息
        Member member = memberInfoService.getMemberById(memberId);
        //计算订单的总价
        long totalPricePay =  wechatOrderService.computeHotelFee(channel_order_no);
        WxOrder wxOrder = new WxOrder();
        wxOrder.setId(CommonUtil.createUUID());
        String body = "支付房费";
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
        wxOrder.setTotalFee(String.valueOf(totalPricePay));
        wxOrder.setTradeType("JSAPI");
        wxOrder.setGoodsTag(channel_order_no);
        wxpayService.insertOrderData(wxOrder);
        Map<String, String> map = wxpayService.orderWeixinPay(body, orderId,totalPricePay, ip,member.getOpenId(), channel_order_no);
        return map;
    }
    //确认房费支付结果
    @PostMapping(value = "/WeChat/console/api/confirmPayHotelFee")
    public Map<String, Object> confirmResult(@RequestParam String memberId,
                                             @RequestParam String orderId,
                                             @RequestParam String order_no) {
        Member member = memberInfoService.getMemberById(memberId);
        return wxpayService.confirmPayHotelFee(member.getOpenId(), orderId, memberId,order_no);
    }

    //房费退款
    @PostMapping(value = "/WeChat/console/api/refundHotelFee")
    public Map<String, String> refundHotelFee(@RequestParam String orderId,
                                              @RequestParam String order_no) {
        return wxpayService.refundHotelFee(orderId,order_no);
    }

    //根据订单号查询相应的发票信息
    @PostMapping(value = "/WeChat/console/api/queryInvoiceInfoByOrderNo")
    public RestError queryInvoiceInfoByOrderNo(@RequestParam String channel_order_no) {
        Invoice invoice =  wechatOrderService.queryRelationByChannel_order_no(channel_order_no);
        return new RestError(invoice);
    }

    //回调地址
    @PostMapping(value = "/wxpay/notifyreceiveOrder")
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
            str = wxpayService.notifyreceiveOrder(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}
