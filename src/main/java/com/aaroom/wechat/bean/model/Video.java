package com.aaroom.wechat.bean.model;

import lombok.Data;

@Data
public class Video {
    /**
     * 通过上传多媒体文件，得到的id
     */
    private String MediaId;
    
    /**
     * 视频消息的标题
     */
    private String Title;
    
    /**
     * 视频消息的描述
     */
    private String Description;

}
