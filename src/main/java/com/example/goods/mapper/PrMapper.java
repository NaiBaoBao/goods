package com.example.goods.mapper;

import com.example.goods.domain.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 */
@Mapper
public interface PrMapper {
    /**
     * 通过id获取Product的po部分信息
     * @param id
     * @return
     */
    public Product getProduct(Integer id);
}
