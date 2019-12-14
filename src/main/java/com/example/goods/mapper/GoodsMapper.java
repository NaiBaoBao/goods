package com.example.goods.mapper;


import com.example.goods.domain.Brand;
import com.example.goods.domain.Goods;
import com.example.goods.domain.GoodsPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsMapper {
    public GoodsPo getGoodsById(Integer id);
    public GoodsPo userGetGoodsById(Integer id);
    public List<GoodsPo> getCategoriesInfoById(Integer id);
    public List<GoodsPo> listGoods(@Param("goodsSn")String goodsSn,@Param("name")String name);
    public List<GoodsPo> adminListGoods(@Param("goodsSn")String goodsSn,@Param("name")String name);
    public int addGoods(GoodsPo goodsPo);
    public int updateGoodsById(GoodsPo goodsPo);
    public int deleteGoodsById(Integer id);
    public int nullBrandGoodsPoList(Integer id);
    public int nullCategoryGoodsPoList(Integer id);
}
