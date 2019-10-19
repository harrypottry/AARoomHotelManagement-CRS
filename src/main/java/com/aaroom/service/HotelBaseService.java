package com.aaroom.service;

import com.aaroom.beans.HotelBase;
import com.aaroom.persistence.HotelBaseMapper;
import com.aaroom.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * @className HotelBaseService
 * @Description 这个类主要是干
 * @Author 张赢
 * @Date 2018/12/24 下午 23:56
 * @Version 1.0
 **/
@Service
public class HotelBaseService {

    @Autowired
    private HotelBaseMapper hotelBaseMapper;


    public HotelBase getById(Integer id){
        return hotelBaseMapper.getById(id);
    }

    public List<HotelBase> getListByMap(Map<String,Object> param){
        return hotelBaseMapper.getListByMap(param);
    }

    public List<HotelBase> getListByParams(Map<String,Object> param){
        return hotelBaseMapper.getListByParams(param);
    }

    public Integer getCountByMap(Map<String,Object> param)throws Exception{
        return hotelBaseMapper.getCountByMap(param);
    }

    public Integer save(HotelBase hotelBase)throws Exception{
            hotelBase.setCreate_time(new Date());
            return hotelBaseMapper.save(hotelBase);
    }

    public Integer modify(HotelBase hotelBase)throws Exception{
        hotelBase.setLast_time(new Date());
        return hotelBaseMapper.modify(hotelBase);
    }

    public Integer removeById(Integer id)throws Exception{
        return hotelBaseMapper.removeById(id);
    }

    /*public Page<List<HotelBase>> queryPageByMap(Map<String,Object> param, Integer pageNo, Integer pageSize)throws Exception{
        Integer total = hotelBaseMapper.getCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<HotelBase> hotelBaseList = hotelBaseMapper.getListByMap(param);
        page.setRows(hotelBaseList);
        return page;
    }*/

    public List<HotelBase> getAllHotelBase(){
        return hotelBaseMapper.getAllHotelBase();
    }

    public List<Map<String,Object>> queryHotelList(Map params){
        return hotelBaseMapper.getAllHotelBaseByAARoom(params);
    }




}
