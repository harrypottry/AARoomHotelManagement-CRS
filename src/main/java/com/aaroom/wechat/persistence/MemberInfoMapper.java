package com.aaroom.wechat.persistence;

import com.aaroom.wechat.bean.Integral;
import com.aaroom.wechat.bean.Member;
import com.aaroom.wechat.bean.MemberLevel;
import com.aaroom.wechat.bean.model.MemberInfoView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository(value = "memberInfoMapper")
public interface MemberInfoMapper {
    MemberLevel getMemberEquity(@Param(value = "memberId")String memberId);

    Integral getIntegralAll(@Param(value = "memberId")String memberId);

    void updateMemberLevel(@Param(value = "openId")String openId, @Param(value = "vipType")String vipType);

    void insertIntegralLog(@Param(value = "i")Integral integral1);

    Member getMemberById(@Param(value = "memberId")String memeberId);

    Member getMemberByOpenId(@Param(value = "openid")String openid);

    void saveMember(@Param(value = "m")Member member);

    MemberInfoView getMemberInfo(@Param(value = "memberId")String memberId);

    void updateMemberInfo(@Param(value = "m")Member member);

    int updateMemberCityAndProvince(Map<String, String> params);

    void insertHotelInfo(@Param(value = "memberId")String memberId, @Param(value = "smHotelCode")String smHotelCode,
                         @Param(value = "hotelName")String hotelName, @Param(value = "bdName")String bdName);

    void updateBuyVipInfo(@Param(value = "m")Member member);

    void updateMemberGiveType(@Param(value = "memberId")String memberId, @Param(value = "i")Integer integer);
}
