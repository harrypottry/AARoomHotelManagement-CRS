package com.aaroom.restcontroller;

import com.aaroom.service.MemberCrsService;
import com.aaroom.wechat.bean.MemberLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: zfzhao
 * @program: AARoomHotelManagement-CRS
 * @description:
 * @create: 2019-03-14 14:20
 **/
@RestController
public class MemberCrsController {

    @Autowired
    private MemberCrsService memberCrsService;

    //获取会员列表
    @GetMapping(value = "/console/api/getMemberList")
    public Map<String, Object> getMemberList(@RequestParam(required = false) String beginTime,
                                              @RequestParam(required = false) String endTime, @RequestParam(required = false) String province, @RequestParam(required = false) String city,
                                              @RequestParam(required = false) String vipType, @RequestParam(required = false) String param, @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                              @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        if (vipType.equals("0")) vipType = null;
        return memberCrsService.getMemberList(beginTime, endTime, province, city, vipType, param, pageNo, pageSize);
    }

    //获取积分流水
    @GetMapping(value = "/console/api/getMemberIntegralFlow")
    public Map<String, Object> getMemberIntegralFlow(@RequestParam String memberId, @RequestParam Integer pageNo,
                                                     @RequestParam Integer pageSize) {
        return memberCrsService.getMemberIntegralFlow(memberId, pageNo, pageSize);
    }

    //获取会员权益
    @GetMapping(value = "/console/api/getMemberEquity")
    public List<MemberLevel> getMemberEquity() {
        return memberCrsService.getMemberEquity();
    }
}
