package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageBean;

import java.util.List;

public interface CategoryService {

    //分页列表查询
    PageBean<Category> findByPage(CategoryPageQueryDTO dto);

    void save(Category category);

    void deleteById(Long id);

    List<Category> findByType(Integer type);
}