package com.shangmei.restcontroller;

import com.shangmei.exception.ProcessException;
import com.shangmei.service.StaffService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StaffController {

    @Autowired
    private StaffService staffService;


    @PostMapping(value = "/wx/console/api/login")
    @ResponseBody
    public Object login(@RequestParam(value = "shopID",defaultValue = "")String shopID,
                        @RequestParam(value = "username",defaultValue = "")String username,
                        @RequestParam(value = "password",defaultValue = "")String password,
                        @RequestParam(value = "data",defaultValue = "")String data,
                        @RequestParam(value = "randomHex",defaultValue = "")String randomHex,
                        @RequestParam(value = "digest",defaultValue = "")String digest,
                        @RequestParam(value = "sn",defaultValue = "")String sn,
                        HttpSession session){
        if (StringUtils.isBlank(username)) {
            throw new ProcessException("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new ProcessException("密码不能为空");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopID", shopID);
        params.put("username", username);
        Map<String, Object> staffMap = staffService.selectStaffForLogin(params);
        if (staffMap == null || staffMap.isEmpty()) {
            throw new ProcessException("用户名错误");
        }
        if (!staffMap.get("opt_flag").toString().equals("1")) {
            throw new ProcessException("门店已关闭");
        }
        String shop_id = staffMap.get("shopID").toString();

        if(!"99999".equals(username) && !"99998".equals(username)){
            String isDongle = staffService.getIsDongle(params);
            if("1".equals(isDongle)){
                if(StringUtils.isBlank(data) || StringUtils.isBlank(randomHex) || StringUtils.isBlank(digest) || StringUtils.isBlank(sn)){
                    throw new ProcessException("获取加密狗信息失败");
                }
            }
        }
        return null;
    }
}
