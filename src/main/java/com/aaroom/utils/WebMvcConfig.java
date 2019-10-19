package com.aaroom.utils;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @className WebMvcConfig
 * @Description
 * @Author 张赢
 * @Date 2018/11/2 16:54
 * @Version 1.0
 **/
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/console/**").addResourceLocations("classpath:/img/");
        registry.addResourceHandler("/console/**").addResourceLocations("file:D:/D:/Develop/Photos/");
    }
}
