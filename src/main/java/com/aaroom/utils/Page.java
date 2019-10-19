package com.aaroom.utils;

import lombok.Data;

import java.util.List;

/**
 * @className Page
 * @Description   分页查询
 * @Author 张赢
 * @Date 2018/11/5 19:17
 * @Version 1.0
 **/

@Data
public class Page<T> {
    //表示第几页，从0开始.当前页
    private int pageIndex;
    //表示每页大小
    private int pageSize;
    //数据库中总的记录条数
    private int count;
    //这次请求的数据结果
    private List<T> dataList;
    //总页数
    private int pageCount;
    //从第几条开始
    private int startIndex;
}
