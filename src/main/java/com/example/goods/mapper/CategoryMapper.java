package com.example.goods.mapper;

import com.example.goods.domain.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    public GoodsCategory getCategoryById(Integer id);
}
