package com.example.goods.controller;

import com.example.goods.domain.*;
//import com.example.goods.feign.FootprintApi;
import com.example.goods.service.GoodsService;
import com.example.goods.util.FileUtils;
import com.example.goods.util.ResponseUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/goodsService")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
//    @Autowired
//    private FootprintApi footprintApi;

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
            retObj = ResponseUtil.fail(502,"图片上传失败");
        }
        return retObj;
    }


    /**
     * 管理员查询商品下的产品，可看下架
     *
     * @param id
     * @return List<ProductVo>，所属该商品的产品列表
     */
    //更改完毕！
    @GetMapping("/goods/{id}/products")
    public List<ProductPo> listProductByGoodsId(@PathVariable(value = "id") Integer id) {
        return goodsService.listProductByGoodsId(id);
    }

    /**
     * 管理员添加商品下的产品
     *
     * @param id
     * @return Product
     */
    //删掉了body的注解！！更改完毕！
    @PostMapping("/goods/{id}/products")
    public ProductPo addProductByGoodsId(@PathVariable Integer id,ProductPo productVo) {
        productVo.setGoodsId(id);
        return goodsService.addProductByGoodsId(productVo);
    }

    /**
     * 根据id获得产品对象
     *
     * @param id
     * @return
     */
    //更改完毕！
    @GetMapping("/products/{id}")
    public ProductPo getProductById(@PathVariable Integer id) {
        return goodsService.getProductById(id);
    }


    /**
     * 管理员修改商品下的某个产品信息
     *
     * @param id
     * @return
     */
    //删掉了body的注解！更改完毕！
    @PutMapping("/products/{id}")
    public ProductPo updateProductById(@PathVariable Integer id, ProductPo productPo) {
        return goodsService.updateProductById(productPo);
    }

    /**
     * 管理员删除商品下的某个产品信息
     *
     * @param id
     * @return
     */
    //@return 无（ResponseUtil.ok()即可）
    //更改完毕！
    @DeleteMapping("/products/{id}")
    public Object deleteProductById(@PathVariable Integer id) {
        Object retObi= ResponseUtil.ok(goodsService.deleteProductById(id));
        return retObi;
    }

    /**
     * 新建商品
     *
     * @param goodsPo
     * @return Goods，即新建的一个商品
     */
    //删掉了body的注解！更改完毕   ？？？？？？？是不是不能建在一级分类下面？更改同理？
    //修改返回报错 responseUtil
    @PostMapping("/goods")
    public GoodsPo addGoods( GoodsPo goodsPo) {
        Integer cid=goodsPo.getGoodsCategoryId();
        if(goodsService.isFirstLevelCategory(cid)==true){
            return null;
        }

            return goodsService.addGoods(goodsPo);
    }

    /**
     * 管理员根据id获取某个商品
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL
     */
    //更改完毕！
    @GetMapping("admin/goods/{id}")
    public GoodsPo getGoodsById(@PathVariable(value = "id") Integer id) {
        return goodsService.getGoodsById(id);
    }

    /**
     * 用户根据id获取某个商品
     *
     * @param id
     * @return GoodsVo，即商品的信息，此URL与WX端是同一个URL，上架！！
     */
    //更改完毕！
    @GetMapping("goods/{id}")
    public GoodsPo userGetGoodsById(@PathVariable(value = "id") Integer id) {
        //footprintAPI.addFootprint()？？？？？？？？？？？？？足迹
        return goodsService.userGetGoodsById(id);
    }


    /**
     * 根据id更新商品信息
     * @param id
     * @param goodsPo
     * @return Goods，修改后的商品信息
     */
    //删掉了body注解！更改完毕！
    //修改错误返回
    @PutMapping("/goods/{id}")
    public GoodsPo updateGoodsById(@PathVariable Integer id, GoodsPo goodsPo) {
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
    //更改完毕！实现级联删除！
    @DeleteMapping("/goods/{id}")
    public Object deleteGoodsById(@PathVariable Integer id) {
        goodsService.deleteGoodsById(id);
        goodsService.deleteProductsByGoodsId(id);
        Object retObi= ResponseUtil.ok();
        return retObi;
    }
    /*
    *
    *
    *
     */

    //管理员通过品牌查询商品，更改完毕，
    @GetMapping("/admins/brands/{id}/goods")
    public List<GoodsPo> getBrandsInfoById(@PathVariable(value = "id")Integer id){
        return goodsService.getBrandsInfoById(id);
    }
    /*
     *
     *!!!!!
     *
     */

    //用户通过品牌查询商品，更改完毕
    @GetMapping("/brands/{id}/goods")
    public List<GoodsPo> userGetBrandsInfoById(@PathVariable(value = "id")Integer id){
        return goodsService.userGetBrandsInfoById(id);
    }
    /**
     * 用户获取分类下的商品信息
     * @param id
     * @return
     */
    //通过，分页,更改完毕！要求上架
    //判断是一级分类还是二级分类，一级分类要返回"是一级分类"
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
        PageInfo<GoodsPo> GoodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =GoodsPageInfo.getList();
        return pagelist;
    }

    /**
     * 管理员获取分类下的商品信息
     * @param id
     * @return
     */
    //通过，分页,更改完毕！
    //判断是一级分类还是二级分类，一级分类要返回"是一级分类"
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
        PageInfo<GoodsPo> GoodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =GoodsPageInfo.getList();
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
// 通过，更改完毕！
    @GetMapping("/goods")
    public List<GoodsPo> listGoods(String goodsSn, String name,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit){
        PageHelper.startPage(page,limit);
        List<GoodsPo> goodsList = goodsService.listGoods(goodsSn,name);
        PageInfo<GoodsPo> GoodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =GoodsPageInfo.getList();
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
// 通过，更改完毕！
    @GetMapping("/admin/goods")
    public List<GoodsPo> adminListGoods(String goodsSn, String name,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer limit){
        PageHelper.startPage(page,limit);
        List<GoodsPo> goodsList = goodsService.adminListGoods(goodsSn,name);
        PageInfo<GoodsPo> GoodsPageInfo = new PageInfo<>(goodsList);
        List<GoodsPo> pagelist =GoodsPageInfo.getList();
        return pagelist;
    }

    /**
     * 管理员根据条件搜索品牌
     *
     * @param page
     * @param limit
     * @return
     */
    //通过,更改完毕
    @GetMapping("/admins/brands")
    public List<BrandPo> listBrandByCondition(@RequestParam Integer id,@RequestParam String name,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer limit) {
        PageHelper.startPage(page,limit);
        List<BrandPo> brandList = goodsService.listBrandByCondition(id,name);
        PageInfo<BrandPo> BrandPageInfo = new PageInfo<>(brandList);
        List<BrandPo> pagelist =BrandPageInfo.getList();
        return pagelist;
    }


    /**
     * 创建一个品牌
     *
     * @param brandPo
     * @return brandPo
     */
    //删掉了body的注解！更改完毕!
    @PostMapping("/brands")
    public BrandPo addBrand(BrandPo brandPo) {
        return goodsService.addBrand(brandPo);
    }

    /**
     * 管理员查看品牌详情,此API与商城端/brands/{id}完全相同
     *
     * @param id
     * @return
     */
    //更改完毕!
    @GetMapping("/admins/brands/{id}")
    public Brand adminGetBrandById(@PathVariable Integer id) {
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
    //更改完毕!
    @GetMapping("/brands/{id}")
    public Brand userGetBrandById(@PathVariable Integer id) {
        Brand brand=goodsService.getBrandById(id);
        brand.setGoodsPoList(goodsService.userGetBrandsInfoById(id));
        return brand;
    }

    /**
     * 修改单个品牌的信息
     *
     * @param id
     * @param brandPo
     * @return
     */
    //删掉了body注解,更改完毕！
    @PutMapping("/brands/{id}")
    public BrandPo updateBrandById(@PathVariable Integer id,BrandPo brandPo) {
        return goodsService.updateBrandById(brandPo);
    }


    /**
     * 删除一个品牌
     *
     * @parambrand
     * @return
     */
    //级联更改，更改完毕！
    @DeleteMapping("/brands/{id}")
    public Object deleteBrandById(@PathVariable(value = "id")Integer id) {
        goodsService.deleteBrandById(id);
        goodsService.nullBrandGoodsPoList(id);
        Object retObi= ResponseUtil.ok();
        return retObi;
    }

    /**
     * 查看所有的分类
     *
     * @return
     */
    //通过，更改完毕
    @GetMapping("/categories")
    public List<GoodsCategoryPo> listGoodsCategory(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer limit) {
        PageHelper.startPage(page,limit);
        List<GoodsCategoryPo> categoryPoList = goodsService.listGoodsCategory();
        PageInfo<GoodsCategoryPo> CategoryPageInfo = new PageInfo<>(categoryPoList);
        List<GoodsCategoryPo> pagelist =CategoryPageInfo.getList();
        return pagelist;
    }

    /**
     * 新建一个分类
     *
     * @param goodsCategoryPo
     * @return
     */
    //删掉了body的注解,更改完毕！修改返回
    @PostMapping("/categories")
    public GoodsCategoryPo addGoodsCategory(GoodsCategoryPo goodsCategoryPo) {
        Integer cid=goodsCategoryPo.getId();
        if(goodsService.isFirstLevelCategory(cid)==true){
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
    //通过，更改完毕
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
    //通过，更改完毕
    @GetMapping("/categories/{id}")
    public Object adminGetGoodsCategoryById(@PathVariable Integer id) {
        GoodsCategory goodsCategory=goodsService.getCategoryById(id);
        goodsCategory.setGoodsPoList(goodsService.getCategoriesInfoById(id));
        return goodsCategory;
    }


    /**
     * 修改分类信息
     *
     * @param id
     * @param goodsCategoryPo
     * @return
     */
    //删掉body注解，更改通过！修改返回
    @PutMapping("/categories/{id}")
    public GoodsCategoryPo updateGoodsCategoryById(@PathVariable Integer id,GoodsCategoryPo goodsCategoryPo) {
        Integer cid=id;
        if(goodsService.isFirstLevelCategory(cid)==true){
            return null;
        }
        return goodsService.updateGoodsCategoryById(goodsCategoryPo);
    }

    /**
     * 删除单个分类
     *
     * @param id
     * @return
     */
    //判断1级，2级，1级有级联删除和级联更改，2级有级联更改，更改完毕！
    //deleteGoodsCategory是删除自己，nullCategoryGoodsPoList是级联更改名下商品
    //point是该目录的pid  null是一级目录，非null是二级
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
    //更改通过
    @GetMapping("/brands")
    public List<BrandPo> listBrand(@RequestParam(defaultValue = "1")  Integer page,
                            @RequestParam(defaultValue = "10")  Integer limit){
        PageHelper.startPage(page,limit);
        List<BrandPo> brandList = goodsService.listBrand();
        PageInfo<BrandPo> BrandPageInfo = new PageInfo<>(brandList);
        List<BrandPo> pagelist =BrandPageInfo.getList();
        return pagelist;
    }

        /**
     * 查看所有一级分类
     *
     * @return
     */
        //更改完毕
    @GetMapping("/categories/l1")
    public List<GoodsCategoryPo> listOneLevelCategory() {
        return goodsService.listOneLevelGoodsCategory();
    }

    /**
     * 获取当前一级分类下的二级分类
     *
     * @param id 分类类目ID
     * @return 当前分类栏目
     */
    //更改完毕
    @GetMapping("categories/l1/{id}/l2")
    public List<GoodsCategoryPo> listSecondLevelGoodsCategoryById(@PathVariable Integer id) {
        return goodsService.listSecondLevelGoodsCategoryById(id);
    }
    //判断商品是否在售（上架）
    @GetMapping("/goods/{id}/isOnSale")
    public boolean isOnSale(@PathVariable Integer id){
        GoodsPo goodsPo=userGetGoodsById(id);
        if(goodsPo.getStatusCode()==0){
            return false;
        }
        return true;
    }

}
