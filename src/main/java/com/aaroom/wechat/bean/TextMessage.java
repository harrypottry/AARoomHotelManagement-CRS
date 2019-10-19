package com.aaroom.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
@Data
public class TextMessage extends Message{

    @XmlElement(name = "MsgType")
    protected String MsgType = "text";

    @XmlElement(name = "Content")
    protected String Content;

}
