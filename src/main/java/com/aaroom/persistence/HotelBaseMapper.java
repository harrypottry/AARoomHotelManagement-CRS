package com.aaroom.persistence;

import com.aaroom.beans.HotelBase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @className HotelBaseMapper
 * @Description 这个类主要是干
 * @Author 张赢
 * @Date 2018/12/24 下午 23:52
 * @Version 1.0
 **/
@Repository
public interface HotelBaseMapper {

	public HotelBase getById(@Param(value = "id") Integer id);

	public List<HotelBase> getListByMap(Map<String, Object> param);

	List<HotelBase> getListByParams(Map<String, Object> param);

	public Integer getCountByMap(Map<String, Object> param)throws Exception;

	public Integer save(HotelBase hotelBase);

	public Integer modify(HotelBase hotelBase)throws Exception;

	public Integer removeById(@Param(value = "id") Integer id);

	public List<HotelBase> getAllHotelBase();


    List<Map<String,Object>> exportHotelBaseName();

	HotelBase getHotelTempByName(String hotel_name);

	Integer saveTemp(HotelBase hotelBase);

	Integer removeByIdTemp(@Param(value = "id") Integer id);

	List<Map<String,Object>> getAllHotelBaseByAARoom(Map params);

    void updateHotelName(Integer hotelId, String hotelName);
}
