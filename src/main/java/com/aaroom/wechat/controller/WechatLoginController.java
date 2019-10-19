package com.aaroom.wechat.controller;

import com.aaroom.utils.WXUtils;
import com.aaroom.wechat.bean.model.MemberInfoView;
import com.aaroom.wechat.bean.model.WechatLoginParams;
import com.aaroom.wechat.service.MemberInfoService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.wechat.Wechat;
import com.aaroom.wechat.WechatToken;
import com.aaroom.wechat.bean.Member;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-07 14:39
 **/
@RestController
public class WechatLoginController {

    @Autowired
    private Wechat wechat;

    @Autowired
    private MemberInfoService memberInfoService;

    @GetMapping(value = "/WeChat/console/api/loginConfirm")
    public Map<String, Object> loginConfirm(@RequestParam String code) {
        Map<String, Object> map = new HashMap<>();
        WechatToken wechatToken = wechat.mediaToken(code, Wechat.HotelManagement);
        Member member = memberInfoService.getMemberByOpenId(wechatToken.getOpenid());
        if (member == null) {
            map.put("status", "fail");
            map.put("msg", "手机号未授权。");
        } else {
            MemberInfoView memberInfoView = memberInfoService.getMemberInfo(member.getId());
            Integer integer = Integer.valueOf(CommonUtil.dateToStr(new Date(), "yyyy"));
            if (memberInfoView.getGiveType() == null || memberInfoView.getGiveType() < integer) {
                Date date = memberInfoView.getBirthday();
                if (null != date) {
                    String birthDate = CommonUtil.dateToStr(date, "MM-dd");
                    String nowDate = CommonUtil.dateToStr(new Date(), "MM-dd");
                    if (birthDate.equals(nowDate)) {
                        memberInfoService.insertIntegralLog(memberInfoView.getId());
                        memberInfoService.updateMemberGiveType(memberInfoView.getId(), integer);
                    }
                }
            }
            map.put("memberId", member.getId());
            map.put("status", "success");
            map.put("openid", wechatToken.getOpenid());
            map.put("phoneNum", member.getPhoneNum());
            map.put("cardLevel", memberInfoView.getCardLevel());
            map.put("cardNum", memberInfoView.getCardNum());
            map.put("levelNum", memberInfoView.getLevelNum());
            map.put("discountPrice", memberInfoView.getDiscountPrice());
        }
        return map;
    }

    @PostMapping(value = "/WeChat/console/api/login")
    public Map<String, Object> login(@RequestBody WechatLoginParams wechatLoginParams) {
        Member member = new Member();
        WechatToken wechatToken = wechat.mediaToken(wechatLoginParams.getCode(), Wechat.HotelManagement);
        String uuid = CommonUtil.createUUID();
        String sessionKey = wechatToken.getSession_key();
        JSONObject jsonObject = WXUtils.getUserInfo(wechatLoginParams.getEncryptedData(), sessionKey, wechatLoginParams.getIv());
        if (jsonObject != null) {
            String phoneNum = jsonObject.getString("phoneNumber");
            member.setPhoneNum(phoneNum);
            member.setId(uuid);
            member.setCardLevelId("v1");
            String cardNum = "7000" + createCharts();
            member.setCardNum(cardNum);
            member.setOpenMode("线上开通");
            member.setExpiryDate(CommonUtil.strToDate("2099-12-31", "yyyy-MM-dd"));
            member.setOpenId(wechatToken.getOpenid());
        }
        return memberInfoService.saveMember(member);
    }

    //获取尚美酒店信息
    @GetMapping(value = "/WeChat/console/api/insertHotelInfo")
    public Map<String, String > insertHotelInfo(@RequestParam String memberId, @RequestParam String smHotelCode) {
        return memberInfoService.insertHotelInfo(memberId, smHotelCode);
    }

    public static String createCharts() {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i <= 7; i++) {
            stringBuffer.append(chars.charAt((int)(Math.random() * 26)));
        }
        return stringBuffer.toString();
    }
}
