package com.aaroom.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Employee {
    private Integer id;

    private String name;

    private String phone;

    private String bank_account;

    private String bank_name;

    private String account_name;

    private Integer status;

    private Integer role_id;

    private String password;

    private String open_id;

    private String union_id;

    private Integer working;

    private Date create_time;

    private Date update_time;

    private Integer creater_id;

    private Integer updater_id;

    private Byte is_active;

}