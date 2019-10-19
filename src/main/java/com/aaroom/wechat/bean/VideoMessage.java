package com.aaroom.wechat.bean;

import com.aaroom.wechat.bean.model.Video;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@Data
public class VideoMessage extends Message {

    @XmlElement(name = "MsgType")
    protected String MsgType = "video";

    @XmlElement(name = "Video")
    private Video Video;


}
