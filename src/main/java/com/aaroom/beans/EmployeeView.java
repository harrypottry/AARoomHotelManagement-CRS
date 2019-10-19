package com.aaroom.beans;

import lombok.Data;

/**
 * Created by sosoda on 2018/10/22.
 */
@Data
public class EmployeeView {
    private Integer id;

    private String name;

    private String phone;

    private String bank_account;

    private String bank_name;

    private String account_name;

    private Integer role_id;

    private String role_name;

    private Integer hotel_id;

    private Integer type;

    public EmployeeView(Employee employee, HotelEmployee hotelEmployee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.phone = employee.getPhone();
        this.bank_account = employee.getBank_account();
        this.bank_name = employee.getBank_name();
        this.account_name = employee.getAccount_name();
        this.role_id = employee.getRole_id();
        this.hotel_id = hotelEmployee.getHotel_id();
        this.type = hotelEmployee.getType();
    }

    public Employee getEmployee(boolean isFull) {
        Employee ret = new Employee();
        ret.setId(this.getId());
        ret.setName(this.getName());
        ret.setPhone(this.getPhone());
        ret.setBank_account(this.getBank_account());
        ret.setBank_name(this.getBank_name());
        ret.setAccount_name(this.getAccount_name());
        ret.setRole_id(this.getRole_id());
        return ret;
    }

    public HotelEmployee getHotelEmployee() {
        HotelEmployee ret = new HotelEmployee();
        ret.setEmployee_id(this.getId());
        ret.setHotel_id(this.getHotel_id());
        ret.setType(this.getType());
        return ret;
    }
}
