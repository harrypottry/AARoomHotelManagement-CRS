package com.aaroom.utils;


import javax.servlet.http.HttpSession;


public class SessionUtil {

    public static Integer getIdBySession(HttpSession session) {
        @SuppressWarnings("unchecked")
        Integer empID = (Integer) session.getAttribute(Constants.Session.key);
        return empID;
    }
    public static String getIdByWechatSession(HttpSession session) {
        @SuppressWarnings("unchecked")
        String weChatId = (String) session.getAttribute(Constants.WechatSession.key);
        return weChatId;
    }
}
