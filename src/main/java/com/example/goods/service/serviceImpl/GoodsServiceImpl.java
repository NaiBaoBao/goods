package com.example.goods.service.serviceImpl;

import com.example.goods.domain.*;
import com.example.goods.mapper.*;
import com.example.goods.service.GoodsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;
@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private GoodsCategoryMapper goodsCategoryMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private BMapper bMapper;

    @Override
    public List<ProductPo> listProductByGoodsId(Integer id) {
        return productMapper.listProductByGoodsId(id);
    }

    @Override
    public int deleteBrandById(Integer id) {
        return brandMapper.deleteBrandById(id);
    }

    @Override
    public int nullBrandGoodsPoList(Integer id) {
        return goodsMapper.nullBrandGoodsPoList(id);
    }

    @Override
    public List<GoodsCategoryPo> listGoodsCategory() {
        return goodsCategoryMapper.listGoodsCategory();
    }

    @Override
    public GoodsPo getGoodsById(Integer id) {
        return goodsMapper.getGoodsById(id);
    }

    @Override
    public GoodsPo userGetGoodsById(Integer id) {
        return goodsMapper.userGetGoodsById(id);
    }

    @Override
    public List<GoodsPo> getCategoriesInfoById(Integer id) {
        return goodsMapper.getCategoriesInfoById(id);
    }

    @Override
    public List<GoodsPo> adminGetCategoriesInfoById(Integer id) {
        return goodsMapper.adminGetCategoriesInfoById(id);
    }


    @Override
    public List<GoodsPo> listGoods(String goodsSn, String name) {
        return goodsMapper.listGoods(goodsSn, name);
    }

    @Override
    public List<GoodsPo> adminListGoods(String goodsSn, String name) {
        return goodsMapper.adminListGoods(goodsSn, name);
    }

    @Override
    public List<BrandPo> listBrandByCondition(Integer id,String name) {
        return brandMapper.listBrandByCondition(id,name);
    }

    @Override
    public ProductPo addProductByGoodsId(ProductPo productPo) {
        productMapper.addProductByGoodsId(productPo);
        return productMapper.getProductById(productPo.getId());
    }

    @Override
    public int deleteProductById(Integer id) {
        productMapper.deleteProductById(id);
        return 0;
    }

    @Override
    public BrandPo getBrandPoById(Integer id) {
        return brandMapper.getBrandPoById(id);
    }

    @Override
    public GoodsPo addGoods(GoodsPo goodsPo) {
        //不能是一级分类下的商品
        goodsMapper.addGoods(goodsPo);
        return goodsMapper.getGoodsById(goodsPo.getId());
    }

    @Override
    public BrandPo addBrand(BrandPo brandPo) {
        brandMapper.addBrand(brandPo);
        return brandMapper.getBrandPoById(brandPo.getId());
    }

    @Override
    public BrandPo updateBrandById(BrandPo brandPo) {
        brandMapper.updateBrandById(brandPo);
        return brandMapper.getBrandPoById(brandPo.getId());
    }

    @Override
    public GoodsCategoryPo addGoodsCategory(GoodsCategoryPo goodsCategoryPo) {
        //不能是二级分类下的分类
        goodsCategoryMapper.addGoodsCategory(goodsCategoryPo);
        return goodsCategoryMapper.getGoodsCategoryPoById(goodsCategoryPo.getId());
    }

    @Override
    public GoodsCategoryPo getGoodsCategoryPoById(Integer id) {
        return goodsCategoryMapper.getGoodsCategoryPoById(id);
    }

    @Override
    public ProductPo getProductById(Integer id) {
        return productMapper.getProductById(id);
    }

    @Override
    public GoodsCategoryPo updateGoodsCategoryById(GoodsCategoryPo goodsCategoryPo) {
        //不能是二级分类下的分类
        goodsCategoryMapper.updateGoodsCategoryById(goodsCategoryPo);
        return goodsCategoryMapper.getGoodsCategoryPoById(goodsCategoryPo.getId());
    }

    @Override
    public int deleteGoodsCategory(Integer id) {
        goodsCategoryMapper.deleteGoodsCategory(id);
        return 0;
    }

    @Override
    public int nullCategoryGoodsPoList(Integer id) {
        return goodsMapper.nullCategoryGoodsPoList(id);
    }

    @Override
    public List<BrandPo> listBrand() {
        return brandMapper.listBrand();
    }

    @Override
    public ProductPo updateProductById(ProductPo productPo) {
        productMapper.updateProductById(productPo);
        return productMapper.getProductById(productPo.getId());
    }

    @Override
    public GoodsPo updateGoodsById(GoodsPo goodsPo) {
        //不能是一级分类下的商品
        goodsMapper.updateGoodsById(goodsPo);
        return goodsMapper.getGoodsById(goodsPo.getId());
    }

    @Override
    public int deleteGoodsById(Integer id) {
        goodsMapper.deleteGoodsById(id);
        return 0;
    }

    @Override
    public int deleteProductsByGoodsId(Integer id) {
        productMapper.deleteProductsByGoodsId(id);
        return 0;
    }

    @Override
    public List<GoodsCategoryPo> listOneLevelGoodsCategory() {
        return goodsCategoryMapper.listOneLevelGoodsCategory();
    }

    @Override
    public List<GoodsCategoryPo> listSecondLevelGoodsCategoryById(Integer id) {
        return goodsCategoryMapper.listSecondLevelGoodsCategoryById(id);
    }

    @Override
    public int deleteSecondLevelCategory(Integer id) {
        goodsCategoryMapper.deleteSecondLevelCategory(id);
        return 0;
    }

    @Override
    public List<Integer> getSecondLevelId(Integer id) {
        return goodsCategoryMapper.getSecondLevelId(id);
    }

    @Override
    public boolean isFirstLevelCategory(Integer id) {
        GoodsCategoryPo goodsCategoryPo = getGoodsCategoryPoById(id);
        Integer p = goodsCategoryPo.getPid();
        if (p == null)
            return true;
        return false;
    }
    @Override
    public List<GoodsPo> getBrandsInfoById(Integer id){
        return goodsMapper.getBrandsInfoById(id);
    }
    @Override
    public List<GoodsPo> userGetBrandsInfoById(Integer id){
        return goodsMapper.getBrandsInfoById(id);
    }
    @Override
    public GoodsCategory getCategoryById(Integer id){
        return categoryMapper.getCategoryById(id);
    }
    @Override
    public Brand getBrandById(Integer id){
        return bMapper.getBrandById(id);
    }
}
