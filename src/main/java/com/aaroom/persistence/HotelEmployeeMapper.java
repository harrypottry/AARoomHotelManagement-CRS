package com.aaroom.persistence;

import com.aaroom.beans.Employee;
import com.aaroom.beans.HotelEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelEmployeeMapper {
    int deleteByPrimaryKey(@Param("hotel_id") String hotel_id, @Param("employee_id") String employee_id);

    int insert(HotelEmployee record);

    int insertSelective(HotelEmployee record);

    HotelEmployee selectByPrimaryKey(@Param("hotel_id") String hotel_id, @Param("employee_id") String employee_id);

    int updateByPrimaryKeySelective(HotelEmployee record);

    HotelEmployee getHotelEmployeeByEmployeeId(@Param("employee_id") Integer employee_id, @Param("type") Integer type);

    HotelEmployee exist(HotelEmployee hotelEmployee);

    void deleteByIds(@Param("hotel_id") String hotel_id, @Param("employee_id") String employee_id);

    List<HotelEmployee> getHotelEmployeesByHotelId(@Param("hotel_id") int hotel_id);

//根据员工id查所属就酒店id (只查酒店id 对象中只有酒店id 没有其他不必要的*)【张赢】
    HotelEmployee getHotelEmployeeByEmployeeIdSoloParam(@Param("employee_id") Integer employee_id);

    //根据酒店id找到对应酒店下的所有员工的中文名字【张赢】
    public List getemployeenamebyloginid(String hotel_id)throws Exception;

    //fast二用根据酒店id找到对应酒店下的所有员工的id +中文名字 弄一个map集合返回去【张赢】
    public List<Integer> getEmployeeNameIdbyloginid(int hotel_id)throws Exception;

    //根据酒店id找到对应酒店下的所有上班员工的中文名字(已上班 working字段为1的员工)【张赢】
    public List getemloyeeinworking(int hotel_id);
    //根据酒店id找到对应酒店下的所有上班员工的中文名字(所有的 不管上不上班)【张赢】
    public List<Employee> GetEmloyee(int hotel_id)throws Exception;


    //
    HotelEmployee getHotelIdByEmpId(Integer employee_id);

    List<HotelEmployee> getHotelIdsByEmpId(Integer employee_id);



}