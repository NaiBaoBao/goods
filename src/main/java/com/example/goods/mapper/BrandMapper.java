package com.example.goods.mapper;

import com.example.goods.domain.Brand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {
    public int deleteBrandById(Integer id);
    public List<Brand> listBrandByCondition(Integer id,String name);
    public Brand getBrandById(Integer id);
    public int addBrand(Brand brand);
    public List<Brand> listBrand();
}
