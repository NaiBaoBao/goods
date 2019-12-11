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
    List<Goods> listGoods();
    List<Brand> listBrandByCondition();
    int insertProductByGoodsId(Product product);
    int deleteProductById(Integer id);
    Brand getBrandById(Integer id);
}
