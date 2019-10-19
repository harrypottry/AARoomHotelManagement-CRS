package com.shangmei.service;


import com.shangmei.persistence.StaffMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class StaffService {

    @Resource
    private StaffMapper staffMapper;

    public String getIsDongle(Map<String, Object> map){
        return staffMapper.getIsDongle(map);
    }
    public Map selectStaffForLogin(Map<String, Object> map){
        return staffMapper.selectStaffForLogin(map);
    }
}
