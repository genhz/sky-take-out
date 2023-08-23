package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    //根据分类id统计套餐数量
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);
}
