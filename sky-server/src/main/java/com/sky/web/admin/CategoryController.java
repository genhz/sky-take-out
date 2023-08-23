package com.sky.web.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //分页列表查询
    @GetMapping("/page")
    public Result findByPage(CategoryPageQueryDTO dto){
        PageBean<Category> pageBean = categoryService.findByPage(dto);
        return Result.success(pageBean);
    }

    @PostMapping
    public Result save(@RequestBody Category category){
        categoryService.save(category);
        return Result.success();
    }
    @DeleteMapping
    public Result deleteById(Long id){
        categoryService.deleteById(id);
        return Result.success();
    }
    //根据类型查询分类列表
    @GetMapping("/list")
    public Result findByType(Integer type){
        List<Category> list =  categoryService.findByType(type);
        return Result.success(list);
    }
}