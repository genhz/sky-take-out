package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    //根据分类id统计菜品数量
    @Select("select count(*) from dish where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);
}
