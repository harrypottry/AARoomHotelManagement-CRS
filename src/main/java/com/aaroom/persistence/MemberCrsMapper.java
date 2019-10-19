package com.aaroom.persistence;

import com.aaroom.model.MemberListView;
import com.aaroom.wechat.bean.Integral;
import com.aaroom.wechat.bean.MemberLevel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-14 14:23
 **/
@Repository
public interface MemberCrsMapper {
    List<MemberListView> getMemberList(@Param(value = "beginTime")String beginTime,
                                       @Param(value = "endTime")String endTime,
                                       @Param(value = "province")String province,
                                       @Param(value = "city")String city,
                                       @Param(value = "vipType")String vipType,
                                       @Param(value = "param")String param);

    List<Integral> getMemberIntegralFlow(@Param(value = "memberId")String memberId);

    List<MemberLevel> getMemberEquity();
}
