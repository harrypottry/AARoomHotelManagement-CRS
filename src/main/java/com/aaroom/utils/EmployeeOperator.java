package com.aaroom.utils;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by sosoda on 2018/6/7.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EmployeeOperator {


}
