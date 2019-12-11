package com.example.goods.mapper;

import com.example.goods.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ProductMapper {
    public List<Product> listProductByGoodsId(Integer id);
    public int addProductByGoodsId(Product product);
    public int deleteProductById(Integer id);
    public Product getProductById(Integer id);
    public int updateProductById(Product product);
}
