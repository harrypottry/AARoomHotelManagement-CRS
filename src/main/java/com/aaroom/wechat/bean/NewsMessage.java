package com.aaroom.wechat.bean;

import com.aaroom.wechat.bean.model.Article;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "xml")
@Data
public class NewsMessage extends Message {

    @XmlElement(name = "MsgType")
    protected String MsgType = "news";

    /**
     * 图文消息个数，限制为10条以内
     */
    @XmlElement(name = "ArticleCount")
    private int ArticleCount;

    /**
     * 多条图文消息信息，默认第一个item为大图
     */
    @XmlElement(name = "Articles")
    private List<Article> Articles;

}
