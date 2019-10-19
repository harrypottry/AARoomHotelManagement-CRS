package com.aaroom.wechat.bean;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Scorpio
 *
 */
@XmlRootElement(name = "xml")
@Data
public class EventMessage extends Message {

    @XmlElement(name = "MsgType")
    protected String MsgType = "event";

    /**
     * 
     * 事件类型
     */
    @XmlElement(name = "Event")
    private String Event;

    // -------CLICK / SCAN / VIEW --------

    /**
     * Event:SCAN
     * <ul>
     * <li>用户未关注时，进行关注后的事件推送：事件KEY值，qrscene_为前缀，后面为二维码的参数值</li>
     * <li>用户已关注时的事件推送：事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id</li>
     * </ul>
     */
    @XmlElement(name = "EventKey")
    private String EventKey;

    // ------- SCAN --------

    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    @XmlElement(name = "Ticket")
    private String Ticket;

    // ------ LOCATION ------------

    /**
     * Event:LOCATION 地理位置纬度
     */
    @XmlElement(name = "Latitude")
    private Float Latitude;

    /**
     * 地理位置经度
     */
    @XmlElement(name = "Longitude")
    private Float Longitude;

    /**
     * 地理位置精度
     */
    @XmlElement(name = "Precision")
    private Float Precision;

    // --------------------

}
