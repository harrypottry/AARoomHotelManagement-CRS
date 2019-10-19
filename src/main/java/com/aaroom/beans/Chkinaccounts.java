package com.aaroom.beans;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Chkinaccounts {
    private Integer id;

    private String shopid;

    private Date atime;

    private Date dtime;

    private Integer chkintype;

    private Integer custometype;

    private Date overtime;

    private Integer ifover;

    private BigDecimal totelfee;

    private String comments;

    private Date createTime;

    private Date updateTime;

    private Integer createId;

    private Integer updateId;

    private Integer isActive;

    private static final long serialVersionUID = 1L;

}