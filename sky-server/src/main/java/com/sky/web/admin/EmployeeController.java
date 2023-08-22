package com.sky.web.admin;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;


    // 员工登录
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        //1. 调用service登录
        Employee employee = employeeService.login(employeeLoginDTO);

        //2. 制作token
        Map<String, Object> claims = new HashMap<>();
        claims.put("empId",employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecret(), jwtProperties.getAdminTtl(), claims);
        // 返回vo
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .token(token)
                .userName(employee.getUsername()).build();

        //3.返回结果
        return Result.success(employeeLoginVO);
    }

    // 员工退出
    @PostMapping("/logout")
    public Result logout(){
        return Result.success();
    }

    // 员工列表分页查询
    @GetMapping("/page")
    public Result findByPage(EmployeePageQueryDTO dto){
        PageBean<Employee> pageBean = employeeService.findByPage(dto);
        return Result.success(pageBean);
    }
    // 员工新增
    @PostMapping
    public Result save(@RequestBody Employee employee) {
        employeeService.save(employee);
        return Result.success();
    }
    //主键查询
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Long id) {
        Employee employee = employeeService.findById(id);
        return Result.success(employee);
    }
    //更新
    @PutMapping
    public Result update(@RequestBody Employee employee){
        employeeService.update(employee);
        return Result.success();
    }
    //停用 启用
    @PostMapping("/status/{status}")
    public Result startOrStop(Long id, @PathVariable("status") Integer status) {

        //1. 构建Employee对象
//        Employee employee = new Employee();
//        employee.setId(id);
//        employee.setStatus(status);
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        //2. 调用service更新
        employeeService.update(employee);

        //3. 返回结果
        return Result.success();
    }
}
