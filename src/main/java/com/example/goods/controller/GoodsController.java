package com.example.goods.controller;

import com.example.goods.domain.*;
import com.example.goods.feign.CommentServiceApi;
import com.example.goods.feign.FootprintServiceApi;
import com.example.goods.mapper.BrandMapper;
import com.example.goods.mapper.CategoryMapper;
import com.example.goods.mapper.GoodsCategoryMapper;
import com.example.goods.mapper.ProductMapper;
import com.example.goods.service.GoodsService;
import com.example.goods.util.FileUtils;
import com.example.goods.util.ResponseUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.goods.util.ResponseUtil.fail;

/**
 * @author
 */
@RestController
@RequestMapping("/goodsService")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    FootprintServiceApi footprintServiceApi;
    @Autowired
    CommentServiceApi commentServiceApi;
    @Resource
    BrandMapper brandMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    GoodsCategoryMapper goodsCategoryMapper;
    @Resource
    ProductMapper productMapper;

    @Value("${web.path}")
    private String path;
    /**
     * 上传图片到服务器 返回图片地址
     *
     * @param file
     * @return url 图片在服务器上的地址
     */
    @PostMapping("/pic")
    public Object pic(@RequestParam("file") MultipartFile file){
        String localPath = "/Users/catherinewang/Desktop/picture";
        String fileName = file.getOriginalFilename();
        String newFile = FileUtils.upload(file,localPath,fileName);
        Object retObj;
        if(newFile != null){
            retObj = ResponseUtil.ok(localPath+"\\"+newFile);
        }else{
            retObj = fail(502,"图片上传失败");
        }
        return retObj;
    }


    /**
     * 管理员查询商品下的产品，可看下架
     *
     * @param id
     * @return List<ProductVo>，所属该商品的产品列表
     */

    @GetMapping("/goods/{id}/products")
    public Object listProductByGoodsId(@PathVariable(value = "id") Integer id) {
        Object retobj;
        retobj=ResponseUtil.ok(goodsService.listProductByGoodsId(id));
        return retobj;
    }

    /**
     * 管理员添加商品下的产品，删掉了body的注解
     *goodsService.addProductByGoodsId(productVo);
     * @param id
     * @return Product
     */
    @PostMapping("/goods/{id}/products")
    public Object addProductByGoodsId(@PathVariable Integer id,ProductPo productVo) {
        Object retobj;
        productVo.setGoodsId(id);
        if(productMapper.addProductByGoodsId(productVo)==1){
            retobj=ResponseUtil.ok(goodsService.getProductById(productVo.getId()));
        }else {
            retobj=ResponseUtil.updatedDataFailed();
        }
        return retobj;
    }

    /**
     * 根据id获得产品对象
     *
     * @param id
     * @return
     */
    @GetMapping("/products/{id}")
    public Object getProductById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        retobj=ResponseUtil.ok(goodsService.getProductById(id));
        return retobj;
    }


    /**
     * 管理员修改商品下的某个产品信息，删掉了body的注解
     *
     * @param id
     * @return
     */
    @PutMapping("/products/{id}")
    public Object updateProductById(@PathVariable Integer id, ProductPo productPo) {
        Object retobj;
        if(productMapper.updateProductById(productPo)==1){
            retobj=ResponseUtil.ok(goodsService.getProductById(id));
        }else {
            retobj=ResponseUtil.updatedDataFailed();
        }
        return retobj;
    }

    /**
     * 管理员删除商品下的某个产品信息，还要实现调用comment删除的内部接口
     *
     * @param id
     * @return 无（ResponseUtil.ok()即可
     */
    @DeleteMapping("/products/{id}")
    public Object deleteProductById(@PathVariable Integer id) {
        Object retObi= ResponseUtil.ok(goodsService.deleteProductById(id));
        commentServiceApi.deletecommentbyproduct(id);
        return retObi;
    }

    /**
     * 新建商品，删掉了body的注解，不能建在一级分类下面，更改同理
     *
     * @param goodsPo
     * @return Goods，即新建的一个商品
     */
    @PostMapping("/goods")
    public Object addGoods( GoodsPo goodsPo) {
        Object retobj;

        Integer cid=goodsPo.getGoodsCategoryId();
        if(goodsService.isFirstLevelCategory(cid)==true){
            retobj=ResponseUtil.badArgumentValue();
            return retobj;
        }
        retobj=ResponseUtil.ok(goodsService.addGoods(goodsPo));
            return retobj;
    }

    /**
     * 管理员根据id获取某个商品
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL
     */
    @GetMapping("admin/goods/{id}")
    public Object getGoodsById(@PathVariable(value = "id") Integer id) {
        return goodsService.getGoodsById(id);
    }

    /**
     * 用户根据id获取某个商品，调用足迹？
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL，上架！！
     *
     * FootprintItemPo footprintItemPo=null;
     *         footprintItemPo.setGoodsId(id);
     *
     *         footprintServiceApi.addFootprint(footprintItemPo);
     */
//    @GetMapping("goods/{id}")
//    public Object userGetGoodsById(HttpServletRequest request, @PathVariable(value = "id") Integer id) {
//        String userId = request.getHeader("id");
//        return goodsService.userGetGoodsById(id);
//    }
    @GetMapping("goods/{id}")
    public Object userGetGoodsById(@PathVariable(value = "id") Integer id) {
        return goodsService.userGetGoodsById(id);
    }


    /**
     * 根据id更新商品信息，删掉了body注解，修改错误返回
     * @param id
     * @param goodsPo
     * @return Goods，修改后的商品信息
     */
    @PutMapping("/goods/{id}")
    public Object updateGoodsById(@PathVariable Integer id, GoodsPo goodsPo) {
        Integer cid=id;
        if(goodsService.isFirstLevelCategory(cid)==true){
            return null;
        }
        return goodsService.updateGoodsById(goodsPo);
    }

    /**
     * 根据id删除商品信息
     *
     * @param id
     * @return 无（即ResponseUtil.ok()即可）
     */
    @DeleteMapping("/goods/{id}")
    public Object deleteGoodsById(@PathVariable Integer id) {
        goodsService.deleteGoodsById(id);
        goodsService.deleteProductsByGoodsId(id);
        Object retObi= ResponseUtil.ok();
        return retObi;
    }
    /**
     * 管理员根据品牌id查询商品
     *
     * @param id
     * @return List<GoodsVo>
     */
    @GetMapping("/admins/brands/{id}/goods")
    public Object getBrandsInfoById(@PathVariable(value = "id")Integer id){
        return goodsService.getBrandsInfoById(id);
    }
    /**
     * 用户根据品牌id查询商品
     *
     * @param id
     * @return List<GoodsVo>
     */
    @GetMapping("/brands/{id}/goods")
    public Object userGetBrandsInfoById(@PathVariable(value = "id")Integer id){
        return goodsService.userGetBrandsInfoById(id);
    }
    /**
     * 用户获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}/goods")
    public Object getCategoriesInfoById(@PathVariable(value = "id")Integer id,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        Integer cid=id;
        if(goodsService.isFirstLevelCategory(cid)==true){
            return null;
        }
        PageHelper.startPage(page,limit);
        List<GoodsPo> goodsList = goodsService.getCategoriesInfoById(id);
        PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =goodsPageInfo.getList();
        return pagelist;
    }

    /**
     * 管理员获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
     * @param id
     * @return
     */
    @GetMapping("/admins/categories/{id}/goods")
    public Object adminGetCategoriesInfoById(@PathVariable(value = "id")Integer id,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer limit) {
        Integer cid=id;
        if(goodsService.isFirstLevelCategory(cid)==true){
            return null;
        }
        PageHelper.startPage(page,limit);
        List<GoodsPo> goodsList = goodsService.adminGetCategoriesInfoById(id);
        PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =goodsPageInfo.getList();
        return pagelist;
    }

    /**
     * 用户根据条件搜索商品，上架
     *
     * @param goodsSn 商品的序列号
     * @param name 商品的名字
     * @param page  第几页
     * @param limit 一页多少
     * @return
     */
    @GetMapping("/goods")
    public Object listGoods(String goodsSn, String name,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit){
        PageHelper.startPage(page,limit);
        List<GoodsPo> goodsList = goodsService.listGoods(goodsSn,name);
        PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =goodsPageInfo.getList();
        return pagelist;
    }

    /**
     * 管理员根据条件搜索商品
     *
     * @param goodsSn 商品的序列号
     * @param name 商品的名字
     * @param page  第几页
     * @param limit 一页多少
     * @return
     */
    @GetMapping("/admin/goods")
    public Object adminListGoods(String goodsSn, String name,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer limit){
        PageHelper.startPage(page,limit);
        List<GoodsPo> goodsList = goodsService.adminListGoods(goodsSn,name);
        PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =goodsPageInfo.getList();
        return pagelist;
    }

    /**
     * 管理员根据条件搜索品牌
     * @param id
     * @param name
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/admins/brands")
    public Object listBrandByCondition(@RequestParam Integer id,@RequestParam String name,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer limit) {
        PageHelper.startPage(page,limit);
        List<BrandPo> brandList = goodsService.listBrandByCondition(id,name);
        PageInfo<BrandPo> brandPageInfo = new PageInfo<>(brandList);
        List<BrandPo> pagelist =brandPageInfo.getList();
        return pagelist;
    }


    /**
     * 创建一个品牌，删掉了body的注解
     *
     * @param brandPo
     * @return brandPo
     */
    @PostMapping("/brands")
    public Object addBrand(BrandPo brandPo) {
        return goodsService.addBrand(brandPo);
    }

    /**
     * 管理员查看品牌详情,此API与商城端/brands/{id}完全相同
     *
     * @param id
     * @return
     */
    @GetMapping("/admins/brands/{id}")
    public Object adminGetBrandById(@PathVariable Integer id) {
        Brand brand=goodsService.getBrandById(id);
        brand.setGoodsPoList(goodsService.getBrandsInfoById(id));
        return brand;
    }

    /**
     *用户查看品牌详情,此API与商城端/brands/{id}完全相同
     *
     * @param id
     * @return
     */
    @GetMapping("/brands/{id}")
    public Object userGetBrandById(@PathVariable Integer id) {
        Brand brand=goodsService.getBrandById(id);
        brand.setGoodsPoList(goodsService.userGetBrandsInfoById(id));
        return brand;
    }

    /**
     * 修改单个品牌的信息，删掉了body注解
     *
     * @param id
     * @param brandPo
     * @return
     */
    @PutMapping("/brands/{id}")
    public Object updateBrandById(@PathVariable Integer id,BrandPo brandPo) {
        return goodsService.updateBrandById(brandPo);
    }


    /**
     * 删除一个品牌，错误是系统内部错误
     *
     * @parambrand
     * @return
     */
    @DeleteMapping("/brands/{id}")
    public Object deleteBrandById(@PathVariable(value = "id")Integer id) {
        goodsService.deleteBrandById(id);
        goodsService.nullBrandGoodsPoList(id);
        Object retObi;
        if(goodsService.deleteBrandById(id)==1){
            retObi=ResponseUtil.ok();
        }else {
            retObi=ResponseUtil.serious();
        }

        return retObi;
    }

    /**
     * 查看所有的分类
     *
     * @return
     */
    @GetMapping("/categories")
    public Object listGoodsCategory(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer limit) {
        PageHelper.startPage(page,limit);
        List<GoodsCategoryPo> categoryPoList = goodsService.listGoodsCategory();
        PageInfo<GoodsCategoryPo> categoryPageInfo = new PageInfo<>(categoryPoList);
        List<GoodsCategoryPo> pagelist =categoryPageInfo.getList();
        return pagelist;
    }

    /**
     * 新建一个分类，删掉了body的注解，修改不能新建在二级分类下
     *
     * @param goodsCategoryPo
     * @return
     */
    @PostMapping("/categories")
    public Object addGoodsCategory(GoodsCategoryPo goodsCategoryPo) {
        Integer cid=goodsCategoryPo.getId();
        if(goodsService.isFirstLevelCategory(cid)==false){
            return null;
        }
        return goodsService.addGoodsCategory(goodsCategoryPo);
    }

    /**
     * 管理员查看单个分类信息
     *
     * @param id
     * @return
     */
    @GetMapping("/admins/categories/{id}")
    public Object getGoodsCategoryById(@PathVariable Integer id) {
        GoodsCategory goodsCategory=goodsService.getCategoryById(id);
        goodsCategory.setGoodsPoList(goodsService.adminGetCategoriesInfoById(id));
        return goodsCategory;
    }

    /**
     * 用户查看单个分类信息
     *
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}")
    public Object adminGetGoodsCategoryById(@PathVariable Integer id) {
        GoodsCategory goodsCategory=goodsService.getCategoryById(id);
        goodsCategory.setGoodsPoList(goodsService.getCategoriesInfoById(id));
        return goodsCategory;
    }


    /**
     * 修改分类信息，删掉body注解，修改不能更改在二级分类下
     *
     * @param id
     * @param goodsCategoryPo
     * @return
     */
    @PutMapping("/categories/{id}")
    public Object updateGoodsCategoryById(@PathVariable Integer id,GoodsCategoryPo goodsCategoryPo) {
        Integer cid=id;
        if(goodsService.isFirstLevelCategory(cid)==true){
            return null;
        }
        return goodsService.updateGoodsCategoryById(goodsCategoryPo);
    }

    /**
     * 删除单个分类，判断1级，2级，1级有级联删除和级联更改，2级有级联更改
     *deleteGoodsCategory是删除自己，nullCategoryGoodsPoList是级联更改名下商品
     * point是该目录的pid  null是一级目录，非null是二级
     * @param id
     * @return
     */
    @DeleteMapping("/categories/{id}")
    public Object deleteGoodsCategory(@PathVariable Integer id) {
        GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(id);
        Integer point= goodsCategoryPo.getPid();
        if(point==null){
            List<Integer> list=goodsService.getSecondLevelId(id);
            for(Integer i : list) {
                goodsService.nullCategoryGoodsPoList(i);
            }
            goodsService.deleteSecondLevelCategory(id);
            goodsService.deleteGoodsCategory(id);

        }
        if(point!=null){
            goodsService.nullCategoryGoodsPoList(id);
            goodsService.deleteGoodsCategory(id);
        }
        Object retObi= ResponseUtil.ok();
        return retObi;
    }



    /**
     * 用户查看所有品牌
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/brands")
    public Object listBrand(@RequestParam(defaultValue = "1")  Integer page,
                            @RequestParam(defaultValue = "10")  Integer limit){
        PageHelper.startPage(page,limit);
        List<BrandPo> brandList = goodsService.listBrand();
        PageInfo<BrandPo> brandPageInfo = new PageInfo<>(brandList);
        List<BrandPo> pagelist =brandPageInfo.getList();
        return pagelist;
    }

        /**
     * 查看所有一级分类
     *
     * @return
     */
    @GetMapping("/categories/l1")
    public Object listOneLevelCategory() {
        return goodsService.listOneLevelGoodsCategory();
    }

    /**
     * 获取当前一级分类下的二级分类
     *
     * @param id 分类类目ID
     * @return 当前分类栏目
     */
    @GetMapping("categories/l1/{id}/l2")
    public Object listSecondLevelGoodsCategoryById(@PathVariable Integer id) {
        return goodsService.listSecondLevelGoodsCategoryById(id);
    }
    /**
     * 判断商品是否在售
     *
     * @param id 商品ID
     * @return
     */
    @GetMapping("/goods/{id}/isOnSale")
    public boolean isOnSale(@PathVariable Integer id){
        GoodsPo goodsPo=goodsService.userGetGoodsById(id);
        if(goodsPo.getStatusCode()==0){
            return false;
        }
        return true;
    }



}
