package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageBean;

public interface EmployeeService {

    // 员工登录
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    PageBean<Employee> findByPage(EmployeePageQueryDTO dto);

    void save(Employee employee);

    Employee findById(Long id);

    void update(Employee employee);
}
