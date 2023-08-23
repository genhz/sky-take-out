package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BusinessException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageBean;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    private DishMapper dishMapper;
    private SetmealMapper setmealMapper;

    @Override
    public PageBean<Category> findByPage(CategoryPageQueryDTO dto) {

        //1. 设置分页条件
        Page<Category> page = new Page<>(dto.getPage(), dto.getPageSize());

        //2. 设置查询条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(dto.getName()), Category::getName, dto.getName())
                .eq(dto.getType() != null, Category::getType, dto.getType())
                .orderByAsc(Category::getSort);
        //3. 执行查询
        categoryMapper.selectPage(page, wrapper);

        //4. 返回结果
        return new PageBean<Category>(page.getTotal(), page.getRecords());
    }

    @Override
    public void save(Category category) {
        //1. 补齐参数
        category.setStatus(1); //默认启用

        //2. 执行保存
        categoryMapper.insert(category);
    }

    @Override
    public void deleteById(Long id) {
        //1. 根据分类id从菜品表中进行统计,如果统计数值>0,则进行提示
        int count1 = dishMapper.countByCategoryId(id);
        if (count1 > 0){
            throw new BusinessException("当前分类下存在菜品,无法删除");
        }

        //2. 根据分类id从套餐表中进行统计,如果统计数值>0,则进行提示
        int count2 = setmealMapper.countByCategoryId(id);
        if (count2 > 0){
            throw new BusinessException("当前分类下存在套餐,无法删除");
        }

        //3. 执行删除
        categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> findByType(Integer type) {
        //1. 封装查询条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType,type);
        //2. 调用查询方法
        List<Category> list = categoryMapper.selectList(wrapper);
        return list;
    }
}