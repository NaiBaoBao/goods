package com.example.goods.mapper;

import com.example.goods.domain.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GoodsCategoryMapper {
    public List<GoodsCategory> listGoodsCategory();
    public int addGoodsCategory(GoodsCategory goodsCategory);
    public GoodsCategory getGoodsCategoryById(Integer id);

}
