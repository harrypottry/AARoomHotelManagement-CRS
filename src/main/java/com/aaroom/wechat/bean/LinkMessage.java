package com.aaroom.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 *
 */
@XmlRootElement(name = "xml")
@Data
public class LinkMessage extends Message{

    @XmlElement(name = "MsgType")
    protected String MsgType = "link";
    
    /**
     * 消息标题
     */
    @XmlElement(name = "Title")
    private String Title;
    
    /**
     * 消息描述
     */
    @XmlElement(name = "Description")
    private String Description;
    
    /**
     * 消息链接
     */
    @XmlElement(name = "Url")
    private String Url;

}
