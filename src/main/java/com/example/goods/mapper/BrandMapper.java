package com.example.goods.mapper;

import com.example.goods.domain.Brand;
import com.example.goods.domain.BrandPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * @author
 */

@Mapper
public interface BrandMapper {
    /**管理员通过id删除品牌
     *
     * @param id
     * @return
     */
    public int deleteBrandById(Integer id);
    /**
     * 管理员根据条件搜索品牌
     * @param id
     * @param name
     * @return
     */
    public List<BrandPo> listBrandByCondition(Integer id,String name);
    /**
     * 根据id搜索品牌Po
     * @param id
     * @return
     */
    public BrandPo getBrandPoById(Integer id);

    /**添加品牌
     *
     * @param brandPo
     * @return
     */
    public int addBrand(BrandPo brandPo);

    /**用户查看所有品牌
     *
     * @return
     */
    public List<BrandPo> listBrand();

    /**更新品牌
     *
     * @param brandPo
     * @return
     */
    public int updateBrandById(BrandPo brandPo);
}
