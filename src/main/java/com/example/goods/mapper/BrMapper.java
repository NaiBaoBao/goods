package com.example.goods.mapper;

import com.example.goods.domain.Brand;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 */
@Mapper
public interface BrMapper {
    /**
 * 通过id获取brand的po部分信息
 * @param id
 * @return
 */
    public Brand getBrandById(Integer id);
}
