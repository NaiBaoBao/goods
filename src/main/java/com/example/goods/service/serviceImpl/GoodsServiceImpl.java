package com.example.goods.service.serviceImpl;

import com.example.goods.domain.Brand;
import com.example.goods.domain.Goods;
import com.example.goods.domain.GoodsCategory;
import com.example.goods.domain.Product;
import com.example.goods.mapper.BrandMapper;
import com.example.goods.mapper.GoodsCategoryMapper;
import com.example.goods.mapper.GoodsMapper;
import com.example.goods.mapper.ProductMapper;
import com.example.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Product> listProductByGoodsId(Integer id){
        return productMapper.listProductByGoodsId(id);
    }
    @Override
    public int deleteBrandById(Integer id) {
        return brandMapper.deleteBrandById(id);
    }
    @Override
    public List<GoodsCategory> listGoodsCategory(){return goodsCategoryMapper.listGoodsCategory();}
    @Override
    public Goods getGoodsById(Integer id){return goodsMapper.getGoodsById(id);}
    @Override
    public List<Goods> getCategoriesInfoById(Integer id){return goodsMapper.getCategoriesInfoById(id);}
    @Override
    public List<Goods> listGoods(String goodsSn,String name){return goodsMapper.listGoods(goodsSn,name);}
    @Override
    public List<Brand> listBrandByCondition(Integer id,String name){return brandMapper.listBrandByCondition(id,name);}
    @Override
    public int addProductByGoodsId(Product product){return productMapper.addProductByGoodsId(product);}
    @Override
    public int deleteProductById(Integer id){return productMapper.deleteProductById(id);}
    @Override
    public Brand getBrandById(Integer id){return brandMapper.getBrandById(id);}
    @Override
    public int addGoods(Goods goods){return goodsMapper.addGoods(goods);}
    @Override
    public int addBrand(Brand brand){return brandMapper.addBrand(brand);}
    @Override
    public int addGoodsCategory(GoodsCategory goodsCategory){return goodsCategoryMapper.addGoodsCategory(goodsCategory);}
    @Override
    public GoodsCategory getGoodsCategoryById(Integer id){return goodsCategoryMapper.getGoodsCategoryById(id);}
    @Override
    public Product getProductById(Integer id){return productMapper.getProductById(id);}
    @Override
    public List<Brand> listBrand(){return brandMapper.listBrand();}
    @Override
    public int updateProductById(Product product){return productMapper.updateProductById(product);}
}
