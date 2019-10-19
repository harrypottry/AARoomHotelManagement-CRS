package com.aaroom.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TencentMapAPIUtils {

    //TODO:地图工具类待完善
    public static String getLocation(String urlString) {
        String resultData = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line; // 获取地址解析结果
            while ((line = in.readLine()) != null) {
                resultData += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            e.getMessage();
        }
        return resultData;
    }
}
