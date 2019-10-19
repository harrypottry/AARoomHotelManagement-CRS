package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class ShopPicture {
    private Integer id;

    private String hotel_id;

    private String pic_type;

    private String url;

    private Date createTime;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;

    private Byte is_active;

    private String comment;


    public ShopPicture() {
    }

    public ShopPicture(String hotel_id, String pic_type, String url, Byte isActive, String comment) {
        this.hotel_id = hotel_id;
        this.pic_type = pic_type;
        this.url = url;
        this.is_active = isActive;
        this.comment = comment;
    }
}