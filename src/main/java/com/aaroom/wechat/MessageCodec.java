package com.aaroom.wechat;

import com.aaroom.wechat.bean.*;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageCodec {

    private static final Logger LOG = LoggerFactory.getLogger(MessageCodec.class);

    public static enum  MessageEnumFactory{
        EVENT(EventMessage.class),
        IMAGE(EventMessage.class),
        LINK(LinkMessage.class),
        LOCATION(LocationMessage.class),
        MUSIC(MusicMessage.class),
        NEWS(NewsMessage.class),
        TEXT(TextMessage.class),
        VIDEO(VideoMessage.class),
        VOICE(VoiceMessage.class);


        public Class<? extends Message> messageClass;

        private MessageEnumFactory(Class<? extends Message> clazz){
            this.messageClass = clazz;
        }

        public Class<? extends Message> getMessageClass() {
            return messageClass;
        }

        private static Map<String,JAXBContext> map = new ConcurrentHashMap<String,JAXBContext>();

        public Message parse(String xmlMessage){
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(xmlMessage.getBytes("UTF-8"));
                Unmarshaller unmarshaller;
                JAXBContext context = map.get(messageClass);
                if(context==null)
                {
                    context = JAXBContext.newInstance(messageClass);
                    map.put(messageClass.toString(), context);
                }
                unmarshaller = context.createUnmarshaller();
                return (Message)unmarshaller.unmarshal(stream);
            } catch (Exception e) {
                LOG.error("解析消息报文错误：" + e.getMessage());
            }
            return  null;
        }

        public String toXml(Message m){
            try {
                JAXBContext jaxbCtx = map.get(messageClass);
                if(jaxbCtx == null){
                    jaxbCtx = JAXBContext.newInstance(m.getClass());
                    map.put(messageClass.toString(), jaxbCtx);
                }

                Marshaller marshaller = jaxbCtx.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                StringWriter sw = new StringWriter();
                marshaller.marshal(m, sw);

                return sw.toString();
            } catch (Exception e) {

            }
            return  null;
        }



    }

    public Message parse(String xml) {
        // 1. xml to map
        // 2. 根据map中的MsgType类型 来区分消息类型
        // 3. 将Map写入对象

        String messageType = getMsgType(xml);
        MessageEnumFactory messageEnum = EnumUtils.getEnum(MessageEnumFactory.class, StringUtils.upperCase(messageType));
        if(messageEnum == null){
            LOG.error("无法识别的微信消息类型："+String.valueOf(messageType));
            return  null;
        }

        return messageEnum.parse(xml);
    }

    public String toXml(Message m) {
        if (m == null) {
            return null;
        }
        MessageEnumFactory messageEnum = EnumUtils.getEnum(MessageEnumFactory.class,StringUtils.upperCase(m.getMsgType()));
        return messageEnum.toXml(m);
    }

    private String getMsgType(String message) {
        return StringUtils.substringBetween(message, "<MsgType><![CDATA[", "]]></MsgType>");
    }

}
