package com.example.goods.mapper;

import com.example.goods.domain.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author
 */
@Mapper

public interface CategoryMapper {
    /**
     * 通过id获取category的po部分
     * @param id
     * @return
     */
    public GoodsCategory getCategoryById(Integer id);
}
