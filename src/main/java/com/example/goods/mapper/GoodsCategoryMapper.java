package com.example.goods.mapper;

import com.example.goods.domain.GoodsCategory;
import com.example.goods.domain.GoodsCategoryPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GoodsCategoryMapper {
    public List<GoodsCategoryPo> listGoodsCategory();
    public int addGoodsCategory(GoodsCategoryPo goodsCategoryPo);
    public GoodsCategoryPo getGoodsCategoryById(Integer id);
    public int updateGoodsCategoryById(GoodsCategoryPo goodsCategoryPo);
    public int deleteGoodsCategory(Integer id);
    public List<GoodsCategoryPo> listOneLevelGoodsCategory();
    public List<GoodsCategoryPo> listSecondLevelGoodsCategoryById(Integer id);
    public int deleteSecondLevelCategory(Integer id);
    public List<Integer> getSecondLevelId(Integer id);
}
