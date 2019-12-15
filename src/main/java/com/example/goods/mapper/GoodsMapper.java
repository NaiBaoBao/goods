package com.example.goods.mapper;


import com.example.goods.domain.Brand;
import com.example.goods.domain.Goods;
import com.example.goods.domain.GoodsPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @author
 */
@Mapper
public interface GoodsMapper {
    /**
     * 管理员根据id获取某个商品
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL
     */
    public GoodsPo getGoodsById(Integer id);
    /**
     * 用户根据id获取某个商品，调用足迹？
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL，上架！！
     */
    public GoodsPo userGetGoodsById(Integer id);
    /**
     * 用户获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
     * @param id
     * @return
     */
    public List<GoodsPo> getCategoriesInfoById(Integer id);
    /**
     * 管理员获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
     * @param id
     * @return
     */
    public List<GoodsPo> adminGetCategoriesInfoById(Integer id);
    /**
     * 用户根据条件搜索商品，上架
     *
     * @param goodsSn 商品的序列号
     * @param name 商品的名字
     * @return
     */
    public List<GoodsPo> listGoods(@Param("goodsSn")String goodsSn,@Param("name")String name);
    /**
     * 管理员根据条件搜索商品
     *
     * @param goodsSn 商品的序列号
     * @param name 商品的名字
     * @return
     */
    public List<GoodsPo> adminListGoods(@Param("goodsSn")String goodsSn,@Param("name")String name);
    /**
     * 新建商品，删掉了body的注解，不能建在一级分类下面，更改同理
     *
     * @param goodsPo
     * @return Goods，即新建的一个商品
     */
    public int addGoods(GoodsPo goodsPo);
    /**
     * 根据id更新商品信息
     * @param goodsPo
     * @return Goods，修改后的商品信息
     */
    public int updateGoodsById(GoodsPo goodsPo);
    /**
     * 根据id删除商品信息
     *
     * @param id
     * @return 无（即ResponseUtil.ok()即可）
     */
    public int deleteGoodsById(Integer id);

    /**
     * 删除品牌时级联更改商品
     * @param id
     * @return
     */
    public int nullBrandGoodsPoList(Integer id);
    /**
     * 删除分类时级联更改商品
     * @param id
     * @return
     */
    public int nullCategoryGoodsPoList(Integer id);
    /**
     * 管理员根据品牌id查询商品
     *
     * @param id
     * @return List<GoodsVo>
     */
    public List<GoodsPo> getBrandsInfoById(Integer id);
    /**
     * 用户根据品牌id查询商品
     *
     * @param id
     * @return List<GoodsVo>
     */
    public List<GoodsPo> userGetBrandsInfoById(Integer id);

}
