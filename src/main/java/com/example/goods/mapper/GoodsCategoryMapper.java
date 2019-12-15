package com.example.goods.mapper;

import com.example.goods.domain.GoodsCategory;
import com.example.goods.domain.GoodsCategoryPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * @author
 */
@Mapper
public interface GoodsCategoryMapper {
    /**
     * 查看所有的分类
     *
     * @return
     */
    public List<GoodsCategoryPo> listGoodsCategory();
    /**
     * 新建一个分类，删掉了body的注解，修改不能新建在二级分类下
     *
     * @param goodsCategoryPo
     * @return
     */
    public int addGoodsCategory(GoodsCategoryPo goodsCategoryPo);

    /**
     * 通过id获得categoryPo
     * @param id
     * @return
     */
    public GoodsCategoryPo getGoodsCategoryPoById(Integer id);
    /**
     * 修改分类信息，删掉body注解，修改不能更改在二级分类下
     *
     * @param goodsCategoryPo
     * @return
     */
    public int updateGoodsCategoryById(GoodsCategoryPo goodsCategoryPo);
    /**
     * 删除单个分类，判断1级，2级，1级有级联删除和级联更改，2级有级联更改
     *deleteGoodsCategory是删除自己，nullCategoryGoodsPoList是级联更改名下商品
     * point是该目录的pid  null是一级目录，非null是二级
     * @param id
     * @return
     */
    public int deleteGoodsCategory(Integer id);

    /**
     * 列举所有的一级分类
     * @return
     */
    public List<GoodsCategoryPo> listOneLevelGoodsCategory();

    /**
     * 列举一级分类下的二级分类
     * @param id
     * @return
     */
    public List<GoodsCategoryPo> listSecondLevelGoodsCategoryById(Integer id);

    /**
     * 删除二级分类
     * @param id
     * @return
     */
    public int deleteSecondLevelCategory(Integer id);

    /**
     * 获得二级分类的id
     * @param id
     * @return
     */
    public List<Integer> getSecondLevelId(Integer id);

}
