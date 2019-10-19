package com.aaroom.service;

import com.aaroom.beans.Employee;
import com.aaroom.beans.EmployeeView;
import com.aaroom.beans.HotelEmployee;
import com.aaroom.persistence.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sosoda on 2018/10/22.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee auth(String phone, String password) {
        return employeeMapper.auth(phone, password);
    }

}
