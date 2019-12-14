package com.example.goods.service;

import com.example.goods.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsService {

//管理员
    List<ProductPo> listProductByGoodsId(Integer id);
    int deleteBrandById(Integer id);
    int nullBrandGoodsPoList(Integer id);

    List<BrandPo> listBrand();

    List<GoodsCategoryPo> listGoodsCategory();
    GoodsPo getGoodsById(Integer id);
    GoodsPo userGetGoodsById(Integer id);
    List<GoodsPo> getCategoriesInfoById(Integer id);
    List<GoodsPo> listGoods(String goodsSn,String name);
    List<GoodsPo> adminListGoods(String goodsSn,String name);
    List<BrandPo> listBrandByCondition();
    ProductPo addProductByGoodsId(ProductPo productPo);
    int deleteProductById(Integer id);
    BrandPo getBrandById(Integer id);
    GoodsPo addGoods(GoodsPo goodsPo);
    BrandPo addBrand(BrandPo brandPo);
    GoodsCategoryPo addGoodsCategory(GoodsCategoryPo goodsCategoryPo);
    GoodsCategoryPo getGoodsCategoryById(Integer id);
    GoodsCategoryPo updateGoodsCategoryById(GoodsCategoryPo goodsCategoryPo);
    int deleteGoodsCategory(Integer id);
    int nullCategoryGoodsPoList(Integer id);
    ProductPo getProductById(Integer id);
    ProductPo updateProductById(ProductPo productPo);
    GoodsPo updateGoodsById(GoodsPo goodsPo);
    int deleteGoodsById(Integer id);
    int deleteProductsByGoodsId(Integer id);
    List<GoodsCategoryPo> listOneLevelGoodsCategory();
    BrandPo updateBrandById(BrandPo brandPo);
    List<GoodsCategoryPo> listSecondLevelGoodsCategoryById(Integer id);
    int deleteSecondLevelCategory(Integer id);
    List<Integer> getSecondLevelId(Integer id);
    boolean isFirstLevelCategory(Integer id);
    //用户端，或者公共（如果区分上下架状态，就没有公共了？）

}
