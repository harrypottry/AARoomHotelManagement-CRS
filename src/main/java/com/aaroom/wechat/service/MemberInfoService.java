package com.aaroom.wechat.service;

import com.aaroom.beans.ShopBase;
import com.aaroom.model.ShangmeiRetView;
import com.aaroom.service.ShopBaseService;
import com.aaroom.utils.CommonUtil;
import com.aaroom.utils.Constants;
import com.aaroom.wechat.bean.Integral;
import com.aaroom.wechat.bean.Member;
import com.aaroom.wechat.bean.MemberLevel;
import com.aaroom.wechat.bean.model.MemberInfoView;
import com.aaroom.wechat.persistence.MemberInfoMapper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-08 16:46
 **/
@Service
public class MemberInfoService {

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    @Autowired
    private WechatOrderService shangmeiOTAService;

    @Autowired
    private ShopBaseService shopBaseService;

    public MemberLevel getMemberEquity(String memberId) {
        MemberLevel memberLevel =  memberInfoMapper.getMemberEquity(memberId);
        return memberLevel;
    }

    public Map<String, Object> memberPromote(String memberId, String vipType) {
        Map<String, Object> map = new HashMap<>();
        Member member = memberInfoMapper.getMemberById(memberId);
        Integral integral = memberInfoMapper.getIntegralAll(memberId);
        Integral integral1 = new Integral();
        if (vipType.equals("v2") && member.getCardLevelId().equals("v1")) {
            if (integral.getIntegralAll() < 5000) {
                map.put("status", "fail");
                map.put("msg", "您的积分总额不足,无法升级!");
                return map;
            } else {
                memberInfoMapper.updateMemberLevel(memberId, vipType);
                integral1.setId(CommonUtil.createUUID());
                integral1.setMemberId(memberId);
                integral1.setIntegralBudget(5000);
                integral1.setCauses("会员升级");
                integral1.setStatus(1);
                integral1.setCreationTime(new Date());
                integral1.setIntegralAll(integral.getIntegralAll() - 5000);
                integral1.setType(1);
                memberInfoMapper.insertIntegralLog(integral1);
            }
        } else if (vipType.equals("v3") && (member.getCardLevelId().equals("v1") || member.getCardLevelId().equals("v2"))) {
            if (member.getCardLevelId().equals("v1") && integral.getIntegralAll() >= 7000) {
                integral1.setIntegralBudget(7000);
                integral1.setIntegralAll(integral.getIntegralAll() - 7000);
            } else if (member.getCardLevelId().equals("v2") && integral.getIntegralAll() >= 2000) {
                integral1.setIntegralBudget(2000);
                integral1.setIntegralAll(integral.getIntegralAll() - 2000);
            } else {
                map.put("status", "fail");
                map.put("msg", "您的积分总额不足,无法升级!");
                return map;
            }
            memberInfoMapper.updateMemberLevel(memberId, vipType);
            integral1.setId(CommonUtil.createUUID());
            integral1.setMemberId(memberId);
            integral1.setCauses("会员升级");
            integral1.setStatus(1);
            integral1.setCreationTime(new Date());
            integral1.setType(1);
            memberInfoMapper.insertIntegralLog(integral1);
        } else {
            map.put("status", "fail");
            map.put("msg", "操作失败！");
            return map;
        }
        map.put("status", "success");
        map.put("msg", "会员升级成功！");
        return map;
    }

    public Integral getIntegralAll(String memberId) {
         return memberInfoMapper.getIntegralAll(memberId);
    }

    public Member getMemberById(String memeberId) {
        return memberInfoMapper.getMemberById(memeberId);
    }

    public Member getMemberByOpenId(String openid) {
        return memberInfoMapper.getMemberByOpenId(openid);
    }

    public Map<String, Object> saveMember(Member member) {
        Map<String, Object> map = new HashMap<>();
        try {
            memberInfoMapper.saveMember(member);
            MemberInfoView memberInfoView = memberInfoMapper.getMemberInfo(member.getId());
            map.put("status", "success");
            map.put("memberId", member.getId());
            map.put("phoneNum", member.getPhoneNum());
            map.put("cardLevel", memberInfoView.getCardLevel());
            map.put("cardNum", memberInfoView.getCardNum());
            map.put("levelNum", memberInfoView.getLevelNum());
            map.put("discountPrice", memberInfoView.getDiscountPrice());
            map.put("msg", "保存成功！");
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("msg", "保存失败！");
            e.printStackTrace();
        }
        return map;
    }

    public MemberInfoView getMemberInfo(String memberId) {
        return memberInfoMapper.getMemberInfo(memberId);
    }

    public Map<String, String> updateMemberInfo(Member member) {
        Map<String, String> map = new HashMap<>();
        try {
            memberInfoMapper.updateMemberInfo(member);
            map.put("status", "success");
            map.put("msg", "修改成功");
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("msg", "修改失败");
            e.printStackTrace();
        }
        return map;
    }

    public int updateMemberCityAndProvince(Map<String, String> params) {
       return memberInfoMapper.updateMemberCityAndProvince(params);
    }

    public Map<String,String> insertHotelInfo(String memberId, String smHotelCode) {
        Map<String, String> map = new HashMap<>();
        try {
            //根据code获取尚美酒店信息
            /*ShangmeiRetView exchange = shangmeiOTAService.exchange(Constants.HOTEL_INFO + smHotelCode, HttpMethod.GET, null, ShangmeiRetView.class);
            JSONObject jsonObject = JSONObject.fromObject(exchange);
            String hotelName = "";
            if ("success".equals(jsonObject.getString("error_msg"))){
                JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.getJSONObject("entity"));
                hotelName = jsonObject1.getString("hotel_name");
            }*/
            String shopName = "";
            String bdName = "";
            if (null != smHotelCode || "".equals(smHotelCode)) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("smhotel_code", smHotelCode);
                map1.put("is_active", 1);
                ShopBase shopBase = shopBaseService.selectByParams(map1);
                if (shopBase != null) {
                    shopName = shopBase.getShop_name();
                    bdName = shopBase.getBd_name();
                }
            }
            memberInfoMapper.insertHotelInfo(memberId, smHotelCode, shopName, bdName);
            map.put("status", "success");
            map.put("msg", "添加成功");
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("msg", "添加失败");
            e.printStackTrace();
        }
        return map;
    }

    public void updateBuyVipInfo(Member member) {
        memberInfoMapper.updateBuyVipInfo(member);
    }

    public void insertIntegralLog(String memberId) {
        MemberLevel memberLevel = memberInfoMapper.getMemberEquity(memberId);
        Integral integral = memberInfoMapper.getIntegralAll(memberId);
        Integral integral1 = new Integral();
        integral1.setId(CommonUtil.createUUID());
        integral1.setMemberId(memberId);
        integral1.setIntegralBudget(memberLevel.getBirthIntegral());
        integral1.setCauses("会员生日赠送");
        integral1.setStatus(1);
        integral1.setCreationTime(new Date());
        if (integral == null) integral1.setIntegralAll(memberLevel.getBirthIntegral());
        else integral1.setIntegralAll(integral.getIntegralAll() + memberLevel.getBirthIntegral());
        integral1.setType(0);
        memberInfoMapper.insertIntegralLog(integral1);
    }

    public void updateMemberGiveType(String memberId, Integer integer) {
        memberInfoMapper.updateMemberGiveType(memberId, integer);
    }
}
