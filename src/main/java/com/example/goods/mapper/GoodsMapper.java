package com.example.goods.mapper;


import com.example.goods.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    public Goods getGoodsById(Integer id);
    public List<Goods> getCategoriesInfoById(Integer id);
    public List<Goods> listGoods(String goodsSn,String name);
    public int addGoods(Goods goods);
}
