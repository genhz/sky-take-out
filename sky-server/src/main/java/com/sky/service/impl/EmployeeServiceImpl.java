package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.UserHolder;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.BusinessException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageBean;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;


    // 员工登录
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        // 1.参数校验
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BusinessException("非法参数");
        }

        // 2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,username);
        Employee employee = employeeMapper.selectOne(wrapper);

        // 3.业务校验
        // 3-1 用户是否存在
        if (employee==null) {
            throw new BusinessException("用户名不存在");
        }

        // 3-2 密码是否正确
        String md5 = SecureUtil.md5(password);
        if (!StrUtil.equals(md5,employee.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3-3 账号是否被禁用
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new BusinessException("此账号被禁用，请联系管理员");
        }

        // 4.登录成功，返回employee
        return employee;
    }

    @Override
    // 分页查询
    public PageBean<Employee> findByPage(EmployeePageQueryDTO dto) {
        //1. 设置分页条件
        Page<Employee> page = new Page<>(dto.getPage(), dto.getPageSize());

        //2. 设置查询条件
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(dto.getName()), Employee::getName, dto.getName())
                .orderByDesc(Employee::getUpdateTime);
        //3. 执行查询
        employeeMapper.selectPage(page, wrapper);

        //4. 返回结果
        return new PageBean<Employee>(page.getTotal(), page.getRecords());
    }

    //新增员工
    @Override
    public void save(Employee employee) {
        //1. 补齐参数
        String passMd5 = SecureUtil.md5(PasswordConstant.DEFAULT_PASSWORD);
        employee.setPassword(passMd5);//加密之后的密码
        employee.setStatus(1);//默认启用

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(UserHolder.get());
//        employee.setUpdateUser(UserHolder.get());

        //2. 调用mapper保存
        employeeMapper.insert(employee);
    }

    @Override
    public Employee findById(Long id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public void update(Employee employee) {
        //1. 补齐参数
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(UserHolder.get());

        //2. 执行更新
        employeeMapper.updateById(employee);
    }
}
