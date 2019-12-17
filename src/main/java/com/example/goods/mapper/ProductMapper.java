package com.example.goods.mapper;

import com.example.goods.domain.Product;
import com.example.goods.domain.ProductPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * @author
 */
@Mapper
public interface ProductMapper {
    /**
     * 列举商品下的货品
     * @param id
     * @return
     */
    public List<ProductPo> listProductByGoodsId(Integer id);

    /**
     * 添加商品下的货品
     * @param productPo
     * @return
     */
    public int addProductByGoodsId(ProductPo productPo);

    /**
     * 删除货品
     * @param id
     * @return
     */
    public int deleteProductById(Integer id);

    /**
     * 获得货品
     * @param id
     * @return
     */
    public ProductPo getProductById(Integer id);

    /**
     * 更改货品
     * @param productPo
     * @return
     */
    public int updateProductById(ProductPo productPo);

    /**级联删除商品下的货品
     *
     * @param id
     * @return
     */
    public int deleteProductsByGoodsId(Integer id);

}
