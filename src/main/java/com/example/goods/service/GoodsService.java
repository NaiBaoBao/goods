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
    List<GoodsPo> adminGetCategoriesInfoById(Integer id);
    List<GoodsPo> listGoods(String goodsSn,String name);
    List<GoodsPo> adminListGoods(String goodsSn,String name);
    List<BrandPo> listBrandByCondition(Integer id,String name);
    ProductPo addProductByGoodsId(ProductPo productPo);
    int deleteProductById(Integer id);

    GoodsPo addGoods(GoodsPo goodsPo);
    BrandPo addBrand(BrandPo brandPo);
    GoodsCategoryPo addGoodsCategory(GoodsCategoryPo goodsCategoryPo);

    GoodsCategoryPo updateGoodsCategoryById(GoodsCategoryPo goodsCategoryPo);
    int deleteGoodsCategory(Integer id);
    int nullCategoryGoodsPoList(Integer id);
    ProductPo getProductById(Integer id);
    ProductPo updateProductById(ProductPo productPo);
    GoodsPo updateGoodsById(GoodsPo goodsPo);
    int deleteGoodsById(Integer id);
    int deleteProductsByGoodsId(Integer id);

    BrandPo updateBrandById(BrandPo brandPo);

    int deleteSecondLevelCategory(Integer id);
    List<Integer> getSecondLevelId(Integer id);
    boolean isFirstLevelCategory(Integer id);

    List<GoodsPo> getBrandsInfoById(Integer id);
    List<GoodsPo> userGetBrandsInfoById(Integer id);

    BrandPo getBrandPoById(Integer id);
    GoodsCategoryPo getGoodsCategoryPoById(Integer id);
    //专门用来返回category的函数1
    GoodsCategory getCategoryById(Integer id);
    //专门用来返回brand的函数1
    Brand getBrandById(Integer id);
    //
    List<GoodsCategoryPo> listOneLevelGoodsCategory();
    //
    List<GoodsCategoryPo> listSecondLevelGoodsCategoryById(Integer id);

}
