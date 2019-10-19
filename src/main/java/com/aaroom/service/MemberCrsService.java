package com.aaroom.service;

import com.aaroom.model.MemberListView;
import com.aaroom.persistence.MemberCrsMapper;
import com.aaroom.utils.Page;
import com.aaroom.utils.PageUtils;
import com.aaroom.wechat.bean.Integral;
import com.aaroom.wechat.bean.MemberLevel;
import com.aaroom.wechat.persistence.MemberInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-14 14:22
 **/
@Service
public class MemberCrsService {

    @Autowired
    private MemberCrsMapper memberCrsMapper;

    @Autowired
    private MemberInfoMapper memberInfoMapper;

    public Map<String, Object> getMemberList(String beginTime, String endTime, String province, String city, String vipType, String param, Integer pageNo, Integer pageSize) {
        List<MemberListView> list = memberCrsMapper.getMemberList(beginTime, endTime, province, city, vipType, param);
        Map<String, Object> map = new HashMap<>();
        for (MemberListView memberListView : list) {
            Integral integral = memberInfoMapper.getIntegralAll(memberListView.getId());
            if (integral == null)
                memberListView.setIntrgralAll(0);
            else
                memberListView.setIntrgralAll(integral.getIntegralAll());
        }
        Page<MemberListView> page = PageUtils.getPageList(list, pageNo, pageSize);
        map.put("data", page);
        return map;
    }

    public Map<String, Object> getMemberIntegralFlow(String memberId, Integer pageNo, Integer pageSize) {
        List<Integral> list = memberCrsMapper.getMemberIntegralFlow(memberId);
        Map<String, Object> map = new HashMap<>();
        Integer integralAll = 0;
        Integer integralKou = 0;
        for (Integral integral : list) {
            if (integral.getType() == 0) {
                integralAll += integral.getIntegralBudget();
            }
            if (integral.getType() == 1) {
                integralKou += integral.getIntegralBudget();
            }
        }
        Page<Integral> page = PageUtils.getPageList(list, pageNo, pageSize);
        map.put("integralAll", integralAll);
        map.put("integralKou", integralKou);
        map.put("curIngetral", integralAll - integralKou);
        map.put("data", page);
        return map;
    }

    public List<MemberLevel> getMemberEquity() {
        return memberCrsMapper.getMemberEquity();
    }
}
