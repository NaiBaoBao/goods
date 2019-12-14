package com.example.goods.mapper;

import com.example.goods.domain.Product;
import com.example.goods.domain.ProductPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ProductMapper {
    public List<ProductPo> listProductByGoodsId(Integer id);
    public int addProductByGoodsId(ProductPo productPo);
    public int deleteProductById(Integer id);
    public ProductPo getProductById(Integer id);
    public int updateProductById(ProductPo productPo);
    public int deleteProductsByGoodsId(Integer id);
}
