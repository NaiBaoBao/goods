package com.example.goods.service.serviceimpl;

import com.example.goods.domain.*;
import com.example.goods.mapper.*;
import com.example.goods.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
/**
 * @author
 */
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
    private BrMapper brMapper;
    @Resource
    private GoMapper goMapper;
    @Resource
    private PrMapper prMapper;

    @Override
    public List<ProductPo> listProductByGoodsId(Integer id) {
        return productMapper.listProductByGoodsId(id);
    }

    @Override
    public int deleteBrandById(Integer id) {
        brandMapper.deleteBrandById(id);
         return 0;

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
    public List<GoodsPo> listGoods(String name) {
        return goodsMapper.listGoods(name);
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
        goodsPo.setGmtCreate(LocalDateTime.now());
        goodsPo.setGmtModified(LocalDateTime.now());
        goodsPo.setStatusCode(0);
        goodsPo.setBeDeleted(false);
        if(goodsMapper.addGoods(goodsPo)==1){
            return  goodsPo;
        }else {
            return null;
        }
    }

    @Override
    public BrandPo addBrand(BrandPo brandPo) {
        if(brandMapper.addBrand(brandPo)==1){
            return brandPo;
        }
        else {
            return null;
        }
    }

    @Override
    public BrandPo updateBrandById(BrandPo brandPo) {
        brandPo.setGmtModified(LocalDateTime.now());
        if(brandMapper.updateBrandById(brandPo)==1){
            return this.getBrandById(brandPo.getId());
        }
        return null;
    }

    @Override
    public GoodsCategoryPo addGoodsCategory(GoodsCategoryPo goodsCategoryPo) {
        goodsCategoryPo.setGmtCreate(LocalDateTime.now());
        goodsCategoryPo.setGmtModified(LocalDateTime.now());
        goodsCategoryPo.setBeDeleted(false);
        if(goodsCategoryMapper.addGoodsCategory(goodsCategoryPo)==1){
            return goodsCategoryPo;
        }else {
            return null;
        }
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
        goodsCategoryPo.setGmtModified(LocalDateTime.now());
        if(goodsCategoryMapper.updateGoodsCategoryById(goodsCategoryPo)==1){
            return goodsCategoryPo;
        }else {
            return null;
        }
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
        productPo.setGmtModified(LocalDateTime.now());
        if(productMapper.updateProductById(productPo)==1){
            return productPo;
        }
        else {
            return null;
        }
    }

    @Override
    public GoodsPo updateGoodsById(GoodsPo goodsPo) {
        //不能是一级分类下的商品
        goodsPo.setGmtModified(LocalDateTime.now());
        if(goodsMapper.updateGoodsById(goodsPo)==1){
            return this.getGoodsById(goodsPo.getId());
        }else {
            return null;
        }

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
    public int isFirstLevelCategory(Integer id) {
        GoodsCategoryPo goodsCategoryPo=goodsCategoryMapper.getGoodsCategoryPoById(id);
        GoodsCategoryPo goodsCategoryPo1;
        Integer pid=goodsCategoryPo.getPid();
        goodsCategoryPo1=goodsCategoryMapper.getGoodsCategoryPoById(pid);
        Integer p = goodsCategoryPo1.getPid();
        if (p == null)
            {return 1;}
        return 0;
    }
    @Override
    public List<GoodsPo> getBrandsInfoById(Integer id){
        return goodsMapper.getBrandsInfoById(id);
    }
    @Override
    public List<GoodsPo> userGetBrandsInfoById(Integer id){
        return goodsMapper.userGetBrandsInfoById(id);
    }
    @Override
    public GoodsCategory getCategoryById(Integer id){
        return categoryMapper.getCategoryById(id);
    }
    @Override
    public Brand getBrandById(Integer id){
        return brMapper.getBrandById(id);
    }

    @Override
    public Goods getGoods(Integer id){
        return goMapper.getGoods(id);
    }

    @Override
    public Goods userGetGoods(Integer id){return goMapper.userGetGoods(id);}

    @Override
    public Product getProduct(Integer id){
        return prMapper.getProduct(id);
    }
    @Override
    public GoodsCategoryPo updateGoodsCategoryPid(GoodsCategoryPo goodsCategoryPo){
        goodsCategoryPo.setGmtModified(LocalDateTime.now());
        if(goodsCategoryMapper.updateGoodsCategoryPid(goodsCategoryPo)==1){
            return this.getGoodsCategoryPoById(goodsCategoryPo.getId());
        }else {
            return null;
        }
    }

}
