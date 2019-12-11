package com.example.goods.controller;

import com.example.goods.domain.Brand;
import com.example.goods.domain.Goods;
import com.example.goods.domain.GoodsCategory;
import com.example.goods.domain.Product;
import com.example.goods.service.GoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/goodsService")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    /**
     * 管理员查询商品下的产品
     *
     * @param id
     * @return
     */
    //通过!!!!!!!!!!!
    @GetMapping("/goods/{id}/products")
    public Object listProductByGoodsId(@PathVariable(value = "id") Integer id) {
        return goodsService.listProductByGoodsId(id);
    }

    /**
     * 管理员添加商品下的产品
     *
     * @param id
     * @return
     */
    //通过！！！！！！！！！！！！！！!!!!!!!!删掉了body的注解！！！！！～～～
    @PostMapping("/goods/{id}/products")
    public Object addProductByGoodsId(@PathVariable Integer id,Product product) {
        product.setGoodsId(id);
        return goodsService.addProductByGoodsId(product);
    }

    /**
     * 管理员修改商品下的某个产品信息
     *
     * @param id
     * @return
     */
    //body删掉了,通过！！
    @PutMapping("/products/{id}")
    public Object updateProductById(@PathVariable Integer id, Product product) {
        return goodsService.updateProductById(product);
    }

    /**
     * 管理员删除商品下的某个产品信息
     *
     * @param id
     * @return
     */
    //通过！！！！！
    @DeleteMapping("/products/{id}")
    public Object deleteProductById(@PathVariable Integer id) {
        return goodsService.deleteProductById(id);
    }

    /**
     * 新建商品
     *
     * @param goods
     * @return
     */
    //通过！！！！！！！！！！！！！！!!!!!!!!删掉了body的注解！！！！！～～～
    @PostMapping("/goods")
    public Object addGoods( Goods goods) {
        return goodsService.addGoods(goods);
    }

    /**
     * 根据id获取某个商品
     *
     * @param id
     * @return
     */
    //通过！！！！！！
    @GetMapping("/goods/{id}")
    public Object getGoodsById(@PathVariable(value = "id") Integer id) {
        return goodsService.getGoodsById(id);
    }

//    /**
//     * 根据id更新商品信息
//     *
//     * @param goodsAllinone
//     * @return
//     */
//    @PutMapping("/goods/{id}")
//    public Object updateGoodsById(@PathVariable Integer id, @RequestBody Goods goods) {
//        return null;
//    }
//
//    /**
//     * 根据id删除商品信息
//     *
//     * @param id
//     * @return
//     */
//    @DeleteMapping("/goods/{id}")
//    public Object deleteGoodsById(@PathVariable Integer id) {
//        return null;
//    }

    /**
     * 获取商品分类信息
     *
     * @return
     */
    // 通过！！！！！！！
    @GetMapping("/categories/{id}/goods")
    public Object getCategoriesInfoById(@PathVariable(value = "id")Integer id) {
        return goodsService.getCategoriesInfoById(id);
    }

    /**
     * 根据条件搜索商品
     *
     * @param goodsSn
     * @param name
     * @param page
     * @param limit
//     * @param sort
//     * @param order
     * @return
     */
// 通过，优先按name查找
    @GetMapping("/goods")
    public Object listGoods(String goodsSn, String name,
                            @RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer limit){
        PageHelper.startPage(page,limit);
        List<Goods> goodsList = goodsService.listGoods(goodsSn,name);
        PageInfo<Goods> GoodsPageInfo = new PageInfo<>(goodsList);
        List<Goods> pagelist =GoodsPageInfo.getList();
        return pagelist;
    }

    /**
     * 管理员根据条件搜索品牌
     *
     * @param id
     * @param name
     * @param page
     * @param limit
     * @return
     */
    //通过
    @GetMapping("/admin/brands")
    public Object listBrandByCondition(Integer id, String name,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        PageHelper.startPage(page,limit);
        List<Brand> brandList = goodsService.listBrandByCondition(id,name);
        PageInfo<Brand> BrandPageInfo = new PageInfo<>(brandList);
        List<Brand> pagelist =BrandPageInfo.getList();
        return pagelist;
    }


    /**
     * 创建一个品牌
     *
     * @param brand
     * @return
     */
    //通过！！！！！！！！！！！！！！!!!!!!!!删掉了body的注解！！！！！～～～
    @PostMapping("/brands")
    public Object addBrand(Brand brand) {
        return goodsService.addBrand(brand);
    }

    /**
     * 查看品牌详情,此API与商城端/brands/{id}完全相同
     *
     * @param id
     * @return
     */
    //通过！！！！
    @GetMapping("/brands/{id}")
    public Object getBrandById(@PathVariable Integer id) {
        return goodsService.getBrandById(id);
    }
//
//    /**
//     * 修改单个品牌的信息
//     *
//     * @param id
//     * @param brand
//     * @return
//     */
//    @PutMapping("/brands/{id}")
//    public Object updateBrandById(@PathVariable Integer id, @RequestBody Brand brand) {
//        return null;
//    }
//
    /**
     * 删除一个品牌
     *
     * @parambrand
     * @return
     */
    //通过！！！！！
    @DeleteMapping("/brands/{id}")
    public Object deleteBrandById(@PathVariable(value = "id")Integer id) {
        return goodsService.deleteBrandById(id);
    }

    /**
     * 查看所有的分类
     *
     * @return
     */
    //通过！！！！
    @GetMapping("/categories")
    public Object listGoodsCategory() {
        return goodsService.listGoodsCategory();
    }

    /**
     * 新建一个分类
     *
     * @param goodsCategory
     * @return
     */
    //通过！！！！！！！！！！！！！！!!!!!!!!删掉了body的注解！！！！！～～～
    @PostMapping("/categories")
    public Object addGoodsCategory(GoodsCategory goodsCategory) {
        return goodsService.addGoodsCategory(goodsCategory);
    }

    /**
     * 查看单个分类信息
     *
     * @param id
     * @return
     */
    //通过！！！！！！
    @GetMapping("/categories/{id}")
    public Object getGoodsCategoryById(@PathVariable Integer id) {
        return goodsService.getGoodsCategoryById(id);
    }

    /**
     * 修改分类信息
     *
     * @param id
     * @param goodsCategory
     * @return
     */
//    @PutMapping("/categories/{id}")
//    public Object updateGoodsCategoryById(@PathVariable Integer id, @RequestBody GoodsCategory goodsCategory) {
//        return null;
//    }
//
//    /**
//     * 删除单个分类
//     *
//     * @param id
//     * @param goodsCategory
//     * @return
//     */
//    @DeleteMapping("/categories/{id}")
//    public Object deleteGoodsCategory(@PathVariable Integer id, @RequestBody GoodsCategory goodsCategory) {
//        return null;
//    }
//
//    /**
//     * 查看所有一级分类
//     *
//     * @return
//     */
//    @GetMapping("/categories/l1")
//    public Object listOneLevelGoodsCategory() {
//        return null;
//    }

    /**
     * 查看所有品牌
     *
     * @param page
     * @param limit
     * @return
     */
    //通过
    @GetMapping("/brands")
    public Object listBrand(@RequestParam Integer page,
                            @RequestParam Integer limit){
        PageHelper.startPage(page,limit);
        List<Brand> brandList = goodsService.listBrand();
        PageInfo<Brand> BrandPageInfo = new PageInfo<>(brandList);
        List<Brand> pagelist =BrandPageInfo.getList();
        return pagelist;
    }

//    /**
//     * 获取当前一级分类下的二级分类？？？一二级怎么判断？？？
//     *
//     * @param id 分类类目ID
//     * @return 当前分类栏目
//     */
//    @GetMapping("categories/l1/{id}/l2")
//    public Object listSecondLevelGoodsCategoryById(Integer id) {
//        return null;
//    }

    /**
     * 根据id获得产品对象
     *
     * @param id
     * @return
     */
    //通过！！！！！
    @GetMapping("/products/{id}")
    public Object getProductById(@PathVariable Integer id) {
        return goodsService.getProductById(id);
    }

}
