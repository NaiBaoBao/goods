package com.example.goods.mapper;

import com.example.goods.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 */
@Mapper
public interface GoMapper {
    /**
     * 管理员通过id获取Goods的po部分信息
     * @param id
     * @return
     */
    public Goods getGoods(Integer id);
    /**
     * 用户通过id获取Goods的po部分信息
     * @param id
     * @return
     */
    public Goods userGetGoods(Integer id);
}

