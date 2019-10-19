package com.aaroom.model;

import lombok.Data;

/**
 * Created by sosoda on 2019/2/20.
 */
@Data
public class ShangmeiRetView<T> {

    private String error_debug;

    private T entity;

    private Integer error_code;

    private String error_msg;
}
