package com.aaroom.utils;

import java.util.Arrays;

/**
 * @className StringSort
 * @Description 这个类主要是干 比较字符串 元素等不等
 * @Author 张赢
 * @Date 2019/1/29 0029下午 18:10
 * @Version 1.0
 **/
public class StringSort {

    public static boolean StringSort(String A ,String B){

        String[] split = A.split(",");
        String[] split1 = B.split(",");

        Arrays.sort( A.split(","));
        Arrays.sort(B.split(","));
        boolean equals = Arrays.equals(split, split1);

        return equals;
    }
}
