package com.aaroom.utils;

import java.util.Collections;
import java.util.List;

/**
 * @author: zfzhao
 * @program: HotelManagement
 * @description: 集合分页
 * @create: 2018-12-28 15:46
 **/
public class PageUtils {

    public static Page getPageList(List list, int pageIndex, int pageSize){
        Page pageList = new Page<>();
        int count = 0;
        if (list != null){
            count = list.size();
        }
        if (count>0){
            //起始第几条：每页开始的第一条数据。即：每页从第几条数据开始
            int startIndex;
            if (pageIndex<=1){
                startIndex=1;
            }else {
                startIndex=(pageIndex-1)*pageSize+1;
            }
            int m=count%pageSize;
            //总页数
            int pagecount;
            if  (m>0){
                pagecount=count/pageSize+1;
            } else {
                pagecount = count / pageSize;
            }
            if (list != null){
                if (pageIndex==pagecount){
                    List listEveryPageData= list.subList((pageIndex-1)*pageSize,count);
                    pageList.setDataList(listEveryPageData);
                }else{
                    List listEveryPageData= list.subList((pageIndex-1)*pageSize,pageSize*(pageIndex));
                    pageList.setDataList(listEveryPageData);
                }
            }else {
                pageList.setDataList(null);
            }
            pageList.setStartIndex(startIndex);
            pageList.setCount(count);
            pageList.setPageIndex(pageIndex);
            pageList.setPageSize(pageSize);
            pageList.setPageCount(pagecount);
        }else {
            pageList.setDataList(Collections.emptyList());
        }
        return pageList;
    }
}
