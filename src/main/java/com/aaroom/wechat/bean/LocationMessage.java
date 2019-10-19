package com.aaroom.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@Data
public class LocationMessage extends Message {

    @XmlElement(name = "MsgType")
    protected String MsgType = "location";
    
    /**
     * 地理位置维度
     */
    @XmlElement(name = "Location_X")
    private String Location_X;

    /**
     * 地理位置经度
     */
    @XmlElement(name = "Location_Y")
    private String Location_Y;

    /**
     * 地图缩放大小
     */
    @XmlElement(name = "Scale")
    private String Scale;

    /**
     * 地理位置信息
     */
    @XmlElement(name = "Label")
    private String Label;

}
