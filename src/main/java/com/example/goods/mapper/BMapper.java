package com.example.goods.mapper;

import com.example.goods.domain.Brand;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BMapper {
    public Brand getBrandById(Integer id);
}
