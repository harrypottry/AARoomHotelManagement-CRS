package com.aaroom.persistence;

import com.aaroom.beans.Employee;
import com.aaroom.beans.EmployeeView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    EmployeeView getEmployeeByPhone(@Param("phone") String phone);

    Employee getEmployeeByPhoneToCheck(@Param("phone") String phone);

    List<EmployeeView> getEmployeeViewListByHotelId(@Param("hotel_id") String hotel_id);
    //两表联查 根据hotelid查到对应的洗衣厂emp对象【张赢】
    List<Employee> getEmployeeWashFactoryListByHotelId(@Param("hotel_id") Integer hotel_id);

    Employee auth(@Param("phone") String phone,
                  @Param("password") String password);

    Employee getEmployeeByOpenId(@Param("open_id") String open_id);

    Employee getempbyname(String name);

    //根据empid查到对应中文名字
    String getEmployeenamebyid(@Param("id") Integer empId);

    //更改阿姨上班状态 0为下班 1为上班
    int workingOnOff(@Param("working") Integer working,
                     @Param("employee_id") Integer employee_id);


    //获取所有的领用人
    List<Map<String,Object>> getContractList(Integer roleId);
}