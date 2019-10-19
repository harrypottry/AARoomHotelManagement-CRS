package com.shangmei.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by liyp on 2019/1/9 0009.
 */
@Data
public class RoomView implements Serializable{
    private static final long serialVersionUID = -7790690645539231821L;

    private String  roomStatus;  //房态

    private Integer floorNum;  //楼层

    private String  roomName;  //房名

    private String  roomType;  //房型
}
