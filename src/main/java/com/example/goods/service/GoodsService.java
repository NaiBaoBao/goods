package com.example.goods.service;

import com.example.goods.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author
 */
@Service
public interface GoodsService {

    /**
     * 管理员查询商品下的产品，可看下架
     *
     * @param id
     * @return List<ProductVo>，所属该商品的产品列表
     */
    List<ProductPo> listProductByGoodsId(Integer id);
    /**
     * 删除一个品牌
     *
     * @param id
     * @return
     */
    int deleteBrandById(Integer id);
    /**
     * 删除分类时级联更改商品
     * @param id
     * @return
     */
    int nullBrandGoodsPoList(Integer id);
    /**
     * 用户查看所有品牌
     *
     * @return
     */
    List<BrandPo> listBrand();
    /**
     * 查看所有的分类
     *
     * @return
     */
    List<GoodsCategoryPo> listGoodsCategory();
    /**
     * 管理员根据id获取某个商品
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL，上架！！
     */
    GoodsPo getGoodsById(Integer id);
    /**
     * 用户根据id获取某个商品，调用足迹
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL
     */
    GoodsPo userGetGoodsById(Integer id);
    /**
     * 用户获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
     * @param id
     * @return
     */
    List<GoodsPo> getCategoriesInfoById(Integer id);
    /**
     * 管理员获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
     * @param id
     * @return
     */
    List<GoodsPo> adminGetCategoriesInfoById(Integer id);
    /**
     * 用户根据条件搜索商品，上架
     *
     *
     * @param name 商品的名字
     * @return
     */
    List<GoodsPo> listGoods(String name);
    /**
     * 管理员根据条件搜索商品
     *
     * @param goodsSn 商品的序列号
     * @param name 商品的名字
     * @return
     */
    List<GoodsPo> adminListGoods(String goodsSn,String name);
    /**
     * 管理员根据条件搜索品牌
     * @param id
     * @param name
     * @return
     */
    List<BrandPo> listBrandByCondition(Integer id,String name);
    /**
     * 添加商品下的货品
     * @param productPo
     * @return
     */
    ProductPo addProductByGoodsId(ProductPo productPo);
    /**
     * 删除货品
     * @param id
     * @return
     */
    int deleteProductById(Integer id);
    /**
     * 新建商品，删掉了body的注解
     * @param goodsPo
     * @return Goods，即新建的一个商品
     */
    GoodsPo addGoods(GoodsPo goodsPo);
    /**添加品牌
     *
     * @param brandPo
     * @return
     */
    BrandPo addBrand(BrandPo brandPo);
    /**
     * 新建一个分类，删掉了body的注解，修改不能新建在二级分类下
     *
     * @param goodsCategoryPo
     * @return
     */
    GoodsCategoryPo addGoodsCategory(GoodsCategoryPo goodsCategoryPo);
    /**
     * 修改分类信息，删掉body注解，修改不能更改在二级分类下
     *
     * @param goodsCategoryPo
     * @return
     */
    GoodsCategoryPo updateGoodsCategoryById(GoodsCategoryPo goodsCategoryPo);
    /**
     * 删除单个分类，判断1级，2级，1级有级联删除和级联更改，2级有级联更改
     * @param id
     * @return
     */
    int deleteGoodsCategory(Integer id);
    /**
     * 删除分类时级联更改商品
     * @param id
     * @return
     */
    int nullCategoryGoodsPoList(Integer id);
    /**
     * 获得货品
     * @param id
     * @return
     */
    ProductPo getProductById(Integer id);
    /**
     * 更改货品
     * @param productPo
     * @return
     */
    ProductPo updateProductById(ProductPo productPo);
    /**
     * 根据id更新商品信息
     * @param goodsPo
     * @return Goods，修改后的商品信息
     */
    GoodsPo updateGoodsById(GoodsPo goodsPo);
    /**
     * 根据id删除商品信息
     *
     * @param id
     * @return 无
     */
    int deleteGoodsById(Integer id);
    /**级联删除商品下的货品
     *
     * @param id
     * @return
     */
    default int deleteProductsByGoodsId(Integer id) {
        return 0;
    }
    /**更新品牌
     *
     * @param brandPo
     * @return
     */
    BrandPo updateBrandById(BrandPo brandPo);
    /**
     * 删除二级分类
     * @param id
     * @return
     */
    int deleteSecondLevelCategory(Integer id);
    /**
     * 获得二级分类的id
     * @param id
     * @return
     */
    List<Integer> getSecondLevelId(Integer id);


    /**
     * 获取品牌下的商品
     * @param id
     * @return
     */
    List<GoodsPo> getBrandsInfoById(Integer id);
    /**
     * 用户根据品牌id查询商品
     *
     * @param id
     * @return List<GoodsVo>
     */
    List<GoodsPo> userGetBrandsInfoById(Integer id);
    /**
     * 根据id搜索品牌Po
     * @param id
     * @return
     */
    BrandPo getBrandPoById(Integer id);
    /**
     * 通过id获得categoryPo
     * @param id
     * @return
     */
    GoodsCategoryPo getGoodsCategoryPoById(Integer id);
    /**
     * 通过id获取category的po部分
     * @param id
     * @return
     */
    GoodsCategory getCategoryById(Integer id);
    /**
     * 通过id获取brand的po部分信息
     * @param id
     * @return
     */
    Brand getBrandById(Integer id);
    /**
     * 列举所有的一级分类
     * @return
     */
    List<GoodsCategoryPo> listOneLevelGoodsCategory();
    /**
     * 列举一级分类下的二级分类
     * @param id
     * @return
     */
    List<GoodsCategoryPo> listSecondLevelGoodsCategoryById(Integer id);

    /**
     * 判断是否是一级分类
     * @param id
     * @return
     */
    int isFirstLevelCategory(Integer id);

}
