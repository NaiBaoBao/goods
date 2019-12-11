package com.example.goods.service;

import com.example.goods.domain.Brand;
import com.example.goods.domain.Goods;
import com.example.goods.domain.GoodsCategory;
import com.example.goods.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsService {


    List<Product> listProductByGoodsId(Integer id);
    int deleteBrandById(Integer id);
    List<GoodsCategory> listGoodsCategory();
    Goods getGoodsById(Integer id);
    List<Goods> getCategoriesInfoById(Integer id);
    List<Goods> listGoods(String goodsSn,String name);
    List<Brand> listBrandByCondition(Integer id,String name);
    int addProductByGoodsId(Product product);
    int deleteProductById(Integer id);
    Brand getBrandById(Integer id);
    int addGoods(Goods goods);
    int addBrand(Brand brand);
    int addGoodsCategory(GoodsCategory goodsCategory);
    GoodsCategory getGoodsCategoryById(Integer id);
    Product getProductById(Integer id);
    List<Brand> listBrand();
    int updateProductById(Product product);
}
