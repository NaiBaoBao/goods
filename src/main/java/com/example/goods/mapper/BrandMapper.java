package com.example.goods.mapper;

import com.example.goods.domain.Brand;
import com.example.goods.domain.BrandPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {
    public int deleteBrandById(Integer id);
    public List<BrandPo> listBrandByCondition();
    public BrandPo getBrandById(Integer id);
    public int addBrand(BrandPo brandPo);
    public List<BrandPo> listBrand();
    public int updateBrandById(BrandPo brandPo);
}
