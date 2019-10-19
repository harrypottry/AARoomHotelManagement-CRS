package com.aaroom.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

@Data
public class Message {
    @XmlElement(name = "ToUserName")
    protected String ToUserName;

    @XmlElement(name = "FromUserName")
    protected String FromUserName;

    @XmlElement(name = "CreateTime")
    protected Long CreateTime;
    @XmlElement(name = "MsgType")
    protected String MsgType;
    
    /**
     * 仅从微信PUSH过来的消息携带
     */
    @XmlElement(name = "MsgId")
    protected Long MsgId;

    /**
     * 位0x0001被标志时，星标刚收到的消息。 仅恢复给微信的消息携带
     */
    @XmlElement(name = "FuncFlag")
    protected Integer FuncFlag;

}
