package com.example.goods.controller;

import com.example.goods.domain.*;
import com.example.goods.feign.CommentServiceApi;
import com.example.goods.feign.FootprintServiceApi;

import com.example.goods.feign.LogServiceApi;

import com.example.goods.mapper.*;
import com.example.goods.service.GoodsService;
import com.example.goods.util.FileUtils;
import com.example.goods.util.JacksonUtil;
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
import java.util.Objects;

import static com.example.goods.util.ResponseUtil.fail;

/**
 * @author
 */
@RestController
//@RequestMapping("/goodsInfoService")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    FootprintServiceApi footprintServiceApi;
    @Autowired
    CommentServiceApi commentServiceApi;
    @Autowired
    LogServiceApi logServiceApi;
    @Resource
    BrandMapper brandMapper;
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    GoodsCategoryMapper goodsCategoryMapper;
    @Resource
    ProductMapper productMapper;
    @Resource
    PrMapper prMapper;
    @Resource
    GoMapper goMapper;

    @Value("${web.path}")
    private String path;
    /**
     * 上传图片到服务器 返回图片地址 删掉了
     *
     * @param file
     * @return url 图片在服务器上的地址
     */
//    @PostMapping("/pics")
//    public Object pic(@RequestParam("file") MultipartFile file){
//        String localPath = "/picture";
//        String fileName = file.getOriginalFilename();
//        String newFile = FileUtils.upload(file,localPath,fileName);
//        Object retObj;
//        if(newFile != null){
//            retObj = ResponseUtil.ok(localPath+"\\"+newFile);
//        }else{
//            retObj = fail(502,"图片上传失败");
//        }
//        return retObj;
//    }


    /**
     * 新建商品，不能建在一级分类下面，更改同理, 新建是下架状态
     *
     * @param goodsPo
     * @return Goods，即新建的一个商品
     */
    @PostMapping("/goods")
    public Object addGoods(@RequestBody GoodsPo goodsPo) {
        Object retobj=new Object();
        Log log=new Log();
        log.setActions("新建商品");
        log.setType(1);
        Integer cid=goodsPo.getGoodsCategoryId();//cid是商品分类id
        Integer id=goodsPo.getId();//id是商品id
        //无分类商品，正常新建
        if(cid==null){
            if(goodsMapper.addGoods(goodsPo)==1) {
                retobj=ResponseUtil.ok(goodsService.getGoodsById(id));
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.addGoodsFail();
                log.setStatusCode(0);
            }
        }else {//有分类商品，Po是其分类
            GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(cid);
            if(goodsCategoryPo.getPid()==0) {//错误建在一级分类下
                retobj=ResponseUtil.addGoodsFail();
                log.setStatusCode(0);
            }
            else if(goodsCategoryPo.getPid()!=0){//建在了二级分类下，正常新建
                if(goodsMapper.addGoods(goodsPo)==1) {
                    retobj=ResponseUtil.ok(goodsService.getGoodsById(id));
                    log.setStatusCode(1);
                }else {
                    retobj=ResponseUtil.addGoodsFail();
                    log.setStatusCode(0);
                }
            }
        }
        return retobj;
    }


    /**
     * 根据id删除商品信息，只能删除下架的商品
     *
     * @param id
     * @return 无（即ResponseUtil.ok()即可）
     */
    @DeleteMapping("/goods/{id}")
    public Object deleteGoodsById(@PathVariable(value = "id") Integer id) {
        Object retObi;
        Log log=new Log();
        log.setActions("删除下架商品");
        log.setType(3);
        GoodsPo goodsPo=goodsService.getGoodsById(id);
        if(goodsPo==null||id==null||id<=0){
            log.setStatusCode(0);
            retObi=ResponseUtil.nullGoodsFail();
        }else {
            if(goodsPo.getStatusCode()==0){
                if(goodsMapper.deleteGoodsById(id)==1){
                    goodsService.deleteProductsByGoodsId(id);
                    retObi=ResponseUtil.ok();
                    log.setStatusCode(1);
                }else {
                    retObi=ResponseUtil.deleteGoodsFail();
                    log.setStatusCode(0);
                }
            }else {
                retObi=ResponseUtil.deleteGoodsFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retObi;
    }

    /**
     * 根据id更新商品信息，删掉了body注解，修改错误返回
     * @param id
     * @param goodsPo
     * @return Goods，修改后的商品信息
     *
     */
    @PutMapping("/goods/{id}")
    public Object updateGoodsById(@PathVariable(value = "id") Integer id, @RequestBody GoodsPo goodsPo) {
        Object retobj=new Object();
        Log log=new Log();
        log.setActions("修改商品");
        log.setType(2);
        if(goodsPo==null||id==null||id<=0){
            log.setStatusCode(0);
            retobj=ResponseUtil.nullGoodsFail();
        }else {
            Integer cid=goodsPo.getGoodsCategoryId();
            if(cid==0){//更改后是无分类商品
                if(goodsMapper.updateGoodsById(goodsPo)==1){
                    retobj=ResponseUtil.ok(goodsService.getGoodsById(id));
                    log.setStatusCode(1);
                }else {
                    retobj=ResponseUtil.updateGoodsFail();
                    log.setStatusCode(0);
                }
            }else if(cid!=0){//更改后是有分类商品
                GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(cid);
                if(goodsCategoryPo.getPid()==0) {//错误更改在一级分类下
                    retobj=ResponseUtil.updateGoodsFail();
                    log.setStatusCode(0);
                }
                else if(goodsCategoryPo.getPid()!=0){//正常更改在二级分类下
                    if(goodsMapper.updateGoodsById(goodsPo)==1) {
                        retobj=ResponseUtil.ok(goodsService.getGoodsById(id));
                        log.setStatusCode(1);
                    }else {
                        retobj=ResponseUtil.updateGoodsFail();
                        log.setStatusCode(0);
                    }
                }
            }
        }
        logServiceApi.addLog(log);
        return retobj;
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
    public Object adminListGoods(@RequestParam String goodsSn, @RequestParam String name,
                                 @RequestParam(defaultValue = "4") Integer page,
                                 @RequestParam(defaultValue = "5") Integer limit){
        Object retobj;
        Log log=new Log();
        log.setActions("条件搜索商品");
        log.setType(0);
        if((goodsSn==null&&name==null)||page==null||limit==null){
            retobj=ResponseUtil.getGoodsListFail();
            log.setStatusCode(0);
        }else {
            PageHelper.startPage(page, limit);
            List<GoodsPo> goodsList = goodsService.adminListGoods(goodsSn, name);
            PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
            List<GoodsPo> pagelist = goodsPageInfo.getList();
            retobj = ResponseUtil.ok(pagelist);
            log.setStatusCode(1);
            logServiceApi.addLog(log);
        }
        return retobj;
    }


    /**
     * 管理员根据id获取某个商品详情,
     *
     * @param id
     * @return Goods
     */
    @GetMapping("/admin/goods/{id}")
    public Object getGoodsById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        Log log = new Log();
        log.setActions("根据id获得商品");
        log.setType(0);
        Goods goods=goodsService.getGoods(id);
        if (id <= 0||goods==null||id==null) {
            retobj = ResponseUtil.nullGoodsFail();
            log.setStatusCode(0);
        } else {
                goods.setBrandPo(brandMapper.getBrandPoById(goods.getBrandId()));
                goods.setGoodsCategoryPo(goodsCategoryMapper.getGoodsCategoryPoById(goods.getGoodsCategoryId()));
                goods.setProductPoList(productMapper.listProductByGoodsId(goods.getId()));
// 内部share              goods.setShareRule();
// 内部discount              goods.setGrouponRule();
// 内部discount             goods.getPresaleRule();
                log.setStatusCode(1);
                retobj = ResponseUtil.ok(goods);
        }
        logServiceApi.addLog(log);
        return retobj;
    }



    /**
     * 管理员查询商品下的产品，可看下架
     *
     * 查找的时候也会失败，比如传参id《=0
     * 每一条管理员的操作都要加：
     * Log log=new Log();
     * log.setActions();//里面是本条的操作
     * log.setType();//这个我们自己组定义一下，0查找，1增加，2修改，3删除
     * 结合错误码，log.setStatusCode();//0为操作失败，1为操作成功
     * @param id
     * @return List<ProductVo>，所属该商品的产品列表
     */

    @GetMapping("/goods/{id}/products")
    public Object listProductByGoodsId(@PathVariable(value = "id") Integer id) {
        Log log=new Log();
        log.setActions("查找商品下货品");
        log.setType(0);
        Object retobj;
        GoodsPo goodsPo=goodsService.getGoodsById(id);
        if(id<=0||goodsPo==null||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.nullGoodsFail();
        }else {
            log.setStatusCode(1);
            retobj=ResponseUtil.ok(goodsService.listProductByGoodsId(id));
        }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     * 管理员添加商品下的产品，删掉了body的注解
     *goodsService.addProductByGoodsId(productVo);
     * @param id
     * @return Product
     */
    @PostMapping("/goods/{id}/products")
    public Object addProductByGoodsId(@PathVariable(value = "id") Integer id,@RequestBody ProductPo productPo) {
        Object retobj;
        Log log=new Log();
        log.setActions("添加商品下产品");
        log.setType(1);
        GoodsPo goodsPo=goodsService.getGoodsById(id);
        if(id<=0||goodsPo==null||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.nullGoodsFail();
        }else {
            productPo.setGoodsId(id);
            if(productMapper.addProductByGoodsId(productPo)==1){
                retobj=ResponseUtil.ok(goodsService.getProductById(productPo.getId()));
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.addProductFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

//    /**
//     * 管理员根据id获得产品对象，删了
//     *
//     * @param id
//     * @return
//     */
//    @GetMapping("/products/{id}")
//    public Object getProductById(@PathVariable(value = "id") Integer id) {
//        Object retobj;
//        retobj=ResponseUtil.ok(goodsService.getProductById(id));
//        return retobj;
//    }


    /**
     * 管理员修改商品下的某个产品信息
     *
     * @param id
     * @return
     */
    @PutMapping("/products/{id}")
    public Object updateProductById(@PathVariable(value = "id") Integer id, @RequestBody ProductPo productPo) {
        Object retobj;
        Log log=new Log();
        log.setActions("修改商品下产品");
        log.setType(2);
        if(id<=0||productPo==null||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.nullGoodsFail();
        }else {
            if(productMapper.updateProductById(productPo)==1){
                retobj=ResponseUtil.ok(goodsService.getProductById(id));
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.updateProductFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     * 管理员删除商品下的某个产品信息，还要实现调用comment删除的内部接口
     *
     * @param id
     * @return 无（ResponseUtil.ok()即可
     */
    @DeleteMapping("/products/{id}")
    public Object deleteProductById(@PathVariable(value = "id") Integer id) {
        Object retObi;
        Log log=new Log();
        log.setActions("删除商品下产品");
        log.setType(3);
        ProductPo productPo=goodsService.getProductById(id);
        if(id<=0||productPo==null||id==null){
            log.setStatusCode(0);
            retObi=ResponseUtil.nullProductFail();
        }else {
            if(productMapper.deleteProductById(id)==1){
                retObi=ResponseUtil.ok();
                commentServiceApi.deletecommentbyproduct(id);
                log.setStatusCode(1);
            }else {
                retObi=ResponseUtil.deleteProductFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retObi;
    }


    /**用户根据id获取某个商品，调用足迹,有UserId的
     *
     * @param id
     * @return Goods
     * ?????????????？？？？？？？??????????头部读不出来
     */
    @GetMapping("/goods/{id}")
    public Object userGetGoodsById(@RequestHeader HttpServletRequest request,@PathVariable(value = "id") Integer id) {
       Object retobj=new Object();
        Goods goods=goodsService.userGetGoods(id);
        String userId = request.getHeader("id");
        Integer i=null;
        if(id<=0||goods==null||id==null){
            retobj=ResponseUtil.nullGoodsFail();
        }else {
            if (userId != null) {
                FootprintItemPo footprintItemPo = new FootprintItemPo();
                i = Integer.valueOf(userId);
                footprintItemPo.setUserId(i);
                footprintItemPo.setGoodsId(id);
                footprintServiceApi.addFootprint(footprintItemPo);
                    goods.setBrandPo(brandMapper.getBrandPoById(goods.getId()));
                    goods.setGoodsCategoryPo(goodsCategoryMapper.getGoodsCategoryPoById(goods.getGoodsCategoryId()));
                    goods.setProductPoList(productMapper.listProductByGoodsId(goods.getId()));
//                goods.setShareRule();
//                goods.setGrouponRule();
//                goods.getPresaleRule();
                    retobj = ResponseUtil.ok(goods);

            }else {
                retobj=ResponseUtil.unlogin();
            }
        }
        return retobj;
    }

//    @GetMapping("/goods/{id}")
//    public Object userGetGoodsById(@PathVariable(value = "id") Integer id) {
//        Object retobj=new Object();
//        Goods goods=new Goods();
//        GoodsPo goodsPo=new GoodsPo();
//                FootprintItemPo footprintItemPo = new FootprintItemPo();
//                footprintItemPo.setUserId(100);
//                footprintItemPo.setGoodsId(id);
//                footprintServiceApi.addFootprint(footprintItemPo);
//                goodsPo=goodsService.getGoodsById(id);
//                if(goodsPo==null){
//                    retobj=ResponseUtil.getGoodsListFail();
//                }else{
//                    goods.setBrandPo(brandMapper.getBrandPoById(goodsPo.getId()));
//                    goods.setGoodsCategoryPo(goodsCategoryMapper.getGoodsCategoryPoById(goodsPo.getGoodsCategoryId()));
//                    goods.setProductPoList(productMapper.listProductByGoodsId(goodsPo.getId()));
////                goods.setShareRule();
////                goods.setGrouponRule();
////                goods.getPresaleRule();
//                    retobj = ResponseUtil.ok(goods);
//                }
//
//
//        return retobj;
//    }
//
//
//
//


//    /**
//     * 管理员根据品牌id查询商品，删掉了
//     *
//     * @param id
//     * @return List<GoodsVo>
//     */
//    @GetMapping("/admin/brands/{id}/goods")
//    public Object getBrandsInfoById(@PathVariable(value = "id")Integer id){
////        Log log=new Log();
////        log.setActions("根据品牌id查商品");
////        log.setType(0);
//        Object retobj;
//        if(id<=0){
////            log.setStatusCode(0);
//            retobj=ResponseUtil.badArgumentValue();
//        }else {
////            log.setStatusCode(1);
//            retobj=ResponseUtil.ok(goodsService.getBrandsInfoById(id));
//        }
////        logServiceApi.addLog(log);
//        return retobj;
//    }
    /**
     * 用户根据品牌id查询商品,上架
     *
     * @param id
     * @return List<GoodsVo>
     */
    @GetMapping("/brands/{id}/goods")
    public Object userGetBrandsInfoById(@PathVariable(value = "id")Integer id){
        Object retobj;
        GoodsPo goodsPo=goodsService.getGoodsById(id);
        if(id<=0||goodsPo==null||id==null){
            retobj=ResponseUtil.nullBrandFail();
        }else {
            retobj=ResponseUtil.ok(goodsService.userGetBrandsInfoById(id));
        }

        return retobj;
    }
    /**
     * 用户获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}/goods")
    public Object getCategoriesInfoById(@PathVariable(value = "id")Integer id,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "5") Integer limit) {
        Object retobj=new Object();
        GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(id);
        if(id<=0||goodsCategoryPo==null||id==null){
            retobj=ResponseUtil.nullCategoryFail();
        }else {
            //id是分类的id，pid是此分类的父分类id
            Integer pid=goodsCategoryPo.getPid();
            if(pid==0){//一级分类
                retobj=ResponseUtil.getGoodsListFail();
            }
            else if(pid!=0){//二级分类
                PageHelper.startPage(page,limit);
                List<GoodsPo> goodsList = goodsService.getCategoriesInfoById(id);
                PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
                List<GoodsPo> pagelist =goodsPageInfo.getList();
                retobj=ResponseUtil.ok(pagelist);
            }
        }
        return retobj;
    }

//    /**
//     * 管理员获取分类下的商品信息，判断是一级分类还是二级分类，一级分类要返回"是一级分类"
//     *删掉了
//     * @param id
//     * @return
//     */
//    @GetMapping("/admin/categories/{id}/goods")
//    public Object adminGetCategoriesInfoById(@PathVariable(value = "id")Integer id,
//                                        @RequestParam(defaultValue = "1") Integer page,
//                                        @RequestParam(defaultValue = "10") Integer limit) {
//        Object retobj=new Object();
//        GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(id);
//        //id是分类的id，pid是此分类的父分类id
//        Integer pid=goodsCategoryPo.getPid();
//        if(pid==null){//一级分类
//            retobj=ResponseUtil.badArgumentValue();
//        }
//        else if(pid!=null){//二级分类
//            PageHelper.startPage(page,limit);
//            List<GoodsPo> goodsList = goodsService.adminGetCategoriesInfoById(id);
//            PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
//            List<GoodsPo> pagelist =goodsPageInfo.getList();
//            retobj=ResponseUtil.ok(pagelist);
//        }
//        return retobj;
//    }

    /**
     * 用户根据条件搜索商品，上架
     *
     *
     * @param name 商品的名字
     * @param page  第几页
     * @param limit 一页多少
     * @return
     */
    @GetMapping("/goods")
    public Object listGoods(@RequestParam String name,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "5") Integer limit){
        Object retobj;
        if(name==null||page==null||limit==null){
            retobj=ResponseUtil.getGoodsListFail();
        }else {
            PageHelper.startPage(page,limit);
            List<GoodsPo> goodsList = goodsService.listGoods(name);
            PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
            List<GoodsPo> pagelist =goodsPageInfo.getList();
            retobj=ResponseUtil.ok(pagelist);
        }
        return retobj;
    }


    /**
     * 管理员根据条件搜索品牌
     * @param id
     * @param name
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/admin/brands")
    public Object listBrandByCondition(@RequestParam Integer id,@RequestParam String name,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "5") Integer limit) {
        Log log=new Log();
        log.setActions("根据条件搜索品牌");
        log.setType(0);
        Object retobj;
        if(id<=0||(id==null&&name==null)||page==null||limit==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.nullBrandFail();
        }
        else {
            PageHelper.startPage(page,limit);
            List<BrandPo> brandList = goodsService.listBrandByCondition(id,name);
            PageInfo<BrandPo> brandPageInfo = new PageInfo<>(brandList);
            List<BrandPo> pagelist =brandPageInfo.getList();
            retobj=ResponseUtil.ok(pagelist);
        }
        logServiceApi.addLog(log);
        return retobj;
    }


    /**
     * 创建一个品牌，删掉了body的注解
     *
     * @param brandPo
     * @return brandPo
     */
    @PostMapping("/brands")
    public Object addBrand(@RequestBody BrandPo brandPo) {
        Object retobj;
        Log log=new Log();
        log.setActions("创建品牌");
        log.setType(1);
        if(brandPo==null){
            retobj=ResponseUtil.nullBrandFail();
        }else {
            if (brandMapper.addBrand(brandPo) == 1) {
                retobj = ResponseUtil.ok(goodsService.getBrandPoById(brandPo.getId()));
            log.setStatusCode(1);
            } else {
                retobj = ResponseUtil.addBrandFail();
            log.setStatusCode(0);
            }
            logServiceApi.addLog(log);
        }
        return retobj;
    }

    /**
     * 管理员查看品牌详情
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * @param id
     * @return
     */
    @GetMapping("/admin/brands/{id}")
    public Object adminGetBrandById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        Log log=new Log();
        log.setActions("查看品牌");
        log.setType(0);
        BrandPo brandPo=brandMapper.getBrandPoById(id);
        if(id<=0||brandPo==null||id==null) {
            log.setStatusCode(0);
            retobj = ResponseUtil.nullBrandFail();
        }else {
            log.setStatusCode(1);
            retobj=ResponseUtil.ok(goodsService.getBrandPoById(id));
        }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     *用户查看品牌详情
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * @param id
     * @return
     */
    @GetMapping("/brands/{id}")
    public Object userGetBrandPoById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        BrandPo brandPo=brandMapper.getBrandPoById(id);
        if(id<=0||brandPo==null||id==null){
            retobj=ResponseUtil.nullBrandFail();
        }else {
            retobj=ResponseUtil.ok(goodsService.getBrandPoById(id));
        }
        return retobj;
    }

    /**
     * 修改单个品牌的信息，删掉了body注解
     *
     * @param id
     * @param brandPo
     * @return
     */
    @PutMapping("/brands/{id}")
    public Object updateBrandById(@PathVariable(value = "id") Integer id,@RequestBody BrandPo brandPo) {
        Object retobj;
        Log log=new Log();
        log.setActions("修改品牌");
        log.setType(2);
        if(id<=0||brandPo==null||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.nullBrandFail();
        }else {
            if(brandMapper.updateBrandById(brandPo)==1){
                retobj=ResponseUtil.ok(goodsService.getBrandPoById(id));
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.updateBrandFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }


    /**
     * 删除一个品牌，错误是系统内部错误
     *
     * @parambrand
     * @return
     */
    @DeleteMapping("/brands/{id}")
    public Object deleteBrandById(@PathVariable(value = "id")Integer id) {
        Object retObi;
        Log log=new Log();
        log.setActions("删除品牌");
        log.setType(3);
        BrandPo brandPo=brandMapper.getBrandPoById(id);
        if(id<=0||brandPo==null||id==null){
            log.setStatusCode(0);
            retObi=ResponseUtil.nullBrandFail();
        }else {
            if(brandMapper.deleteBrandById(id)==1){
                goodsService.nullBrandGoodsPoList(id);
                retObi=ResponseUtil.ok();
                log.setStatusCode(1);
            }else {
                retObi=ResponseUtil.deleteBrandFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retObi;
    }

    /**
     * 管理员查看所有的分类
     *
     * @return
     */
    @GetMapping("/categories")
    public Object adminListGoodsCategory(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer limit) {
        Object retobj;
        Log log=new Log();
        log.setActions("查看分类");
        log.setType(0);
        if(page==null||limit==null){
            retobj=ResponseUtil.getCategoryListFail();
        }else {
            PageHelper.startPage(page,limit);
            List<GoodsCategoryPo> categoryPoList = goodsService.listGoodsCategory();
            PageInfo<GoodsCategoryPo> categoryPageInfo = new PageInfo<>(categoryPoList);
            List<GoodsCategoryPo> pagelist =categoryPageInfo.getList();
            retobj=ResponseUtil.ok(pagelist);
            log.setStatusCode(1);
        }
        logServiceApi.addLog(log);
        return retobj;
    }
//    /**
//     * 用户查看所有的分类，被删掉
//     *
//     * @return
//     */
//    @GetMapping("/categories")
//    public Object listGoodsCategory(@RequestParam(defaultValue = "1") Integer page,
//                                                   @RequestParam(defaultValue = "10") Integer limit) {
//        Object retobj;
//        PageHelper.startPage(page,limit);
//        List<GoodsCategoryPo> categoryPoList = goodsService.listGoodsCategory();
//        PageInfo<GoodsCategoryPo> categoryPageInfo = new PageInfo<>(categoryPoList);
//        List<GoodsCategoryPo> pagelist =categoryPageInfo.getList();
//        retobj=ResponseUtil.ok(pagelist);
//        return retobj;
//    }

    /**
     * 管理员查看单个分类信息
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}")
    public Object getGoodsCategoryPoById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        Log log=new Log();
        log.setActions("查看分类");
        log.setType(0);
        GoodsCategoryPo goodsCategoryPo=goodsCategoryMapper.getGoodsCategoryPoById(id);
        if(id<=0||goodsCategoryPo==null||id==null){
            retobj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        }else {
            retobj=ResponseUtil.ok(goodsService.getGoodsCategoryPoById(id));
            log.setStatusCode(1);
        }
        logServiceApi.addLog(log);
        return retobj;
    }

//    /**
//     * 用户查看单个分类信息，被删掉了
//     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//     * @param id
//     * @return
//     */
//    @GetMapping("/categories/{id}")
//    public Object adminGetGoodsCategoryById(@PathVariable Integer id) {
//        Object retobj;
//        if(id<=0){
//            retobj=ResponseUtil.badArgumentValue();
//        }else {
//            retobj=ResponseUtil.ok(goodsService.getGoodsCategoryPoById(id));
//        }
//        return retobj;
//    }


    /**
     * 新建一个分类，删掉了body的注解，修改不能新建在二级分类下
     * @param goodsCategoryPo
     * @return
     */
    @PostMapping("/categories")
    public Object addGoodsCategory(@RequestBody GoodsCategoryPo goodsCategoryPo) {
        Object retobj = null;
        Log log=new Log();
        log.setActions("新建分类");
        log.setType(1);
        if(goodsCategoryPo.getPid()==null){
            if(goodsCategoryMapper.addGoodsCategory(goodsCategoryPo)==1){
                retobj=ResponseUtil.ok(goodsService.getGoodsCategoryPoById(goodsCategoryPo.getId()));
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.addCategoryFail();
                log.setStatusCode(0);
            }
        }
        else if(goodsCategoryPo.getPid()!=null){
            GoodsCategoryPo goodsCategoryPo1;
            Integer p=goodsCategoryPo.getPid();
            goodsCategoryPo1=goodsService.getGoodsCategoryPoById(p);
            if(goodsCategoryPo1.getPid()!=null){
                retobj=ResponseUtil.addCategoryFail();
                log.setStatusCode(0);
            }
            else {
                if(goodsCategoryMapper.addGoodsCategory(goodsCategoryPo)==1){
                    retobj=ResponseUtil.ok(goodsService.getGoodsCategoryPoById(goodsCategoryPo.getId()));
                    log.setStatusCode(1);
                }else {
                    retobj=ResponseUtil.addCategoryFail();
                    log.setStatusCode(0);
                }
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     * 修改分类信息，删掉body注解，修改不能更改在一级或二级分类
     * 二级到一级会影响到商品的归属，一级到二级影响其他子分类
     * 所以不可修改pid。只能修改name和图片
     * @param id
     * @param goodsCategoryPo
     * @return
     */
    @PutMapping("/categories/{id}")
    public Object updateGoodsCategoryById(@PathVariable(value = "id") Integer id,@RequestBody GoodsCategoryPo goodsCategoryPo) {
        Object retobj;
        Log log=new Log();
        log.setActions("修改分类");
        log.setType(2);
        if(id<=0||goodsCategoryPo==null||id==null){
            retobj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        }else {
            if(goodsCategoryMapper.updateGoodsCategoryById(goodsCategoryPo)==1){
                retobj=ResponseUtil.ok(goodsService.getGoodsCategoryPoById(id));
                log.setStatusCode(1);
            }else {
                log.setStatusCode(0);
                retobj=ResponseUtil.updateCategoryFail();
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     *修改二级分类在一级分类下的位置
     *body包含pid(只能修改pid不为空的项，且修改后pid不可为空)
     * @param id
     * @param goodsCategoryPo
     * @return
     */
    @PutMapping("/categories/l2/{id}")
    public Object updateGoodsCategoryPid(@PathVariable(value = "id") Integer id,@RequestBody GoodsCategoryPo goodsCategoryPo) {
        Object retobj;
        Log log=new Log();
        log.setActions("修改二级分类的父分类");
        log.setType(2);
        Integer oldPid=goodsCategoryMapper.getGoodsCategoryPoById(id).getPid();
        Integer newPid=goodsCategoryPo.getPid();
        if(id<=0||oldPid<0||newPid<0||goodsCategoryPo==null||id==null){
            retobj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        }else {//判断一下新的父分类是不是一级分类
            GoodsCategoryPo goodsCategoryPo1=goodsCategoryMapper.getGoodsCategoryPoById(newPid);
            if(goodsCategoryPo1.getPid()==0){//新父类是一级分类
                if(goodsCategoryMapper.updateGoodsCategoryPid(goodsCategoryPo)==1){
                    retobj=ResponseUtil.ok(goodsService.getGoodsCategoryPoById(id));
                    log.setStatusCode(1);
                }else {
                    log.setStatusCode(0);
                    retobj=ResponseUtil.updateCategoryFail();
                }
            }else {
                retobj=ResponseUtil.updateCategoryFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     * 删除单个分类，判断1级，2级，1级有级联删除和级联更改，2级有级联更改
     *deleteGoodsCategory是删除自己，nullCategoryGoodsPoList是级联更改名下商品
     * point是该目录的pid  null是一级目录，非null是二级
     * @param id
     * @return
     *
     */
    @DeleteMapping("/categories/{id}")
    public Object deleteGoodsCategory(@PathVariable(value = "id") Integer id) {
        Object retObj;
        Log log=new Log();
        log.setActions("删除分类");
        log.setType(3);
        GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(id);
        if(id<=0||goodsCategoryPo==null||id==null){
            retObj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        }else {
            Integer point= goodsCategoryPo.getPid();
            if(point==0){
                List<Integer> list=goodsService.getSecondLevelId(id);
                for(Integer i : list) {
                    goodsService.nullCategoryGoodsPoList(i);
                }
                goodsService.deleteSecondLevelCategory(id);
                goodsService.deleteGoodsCategory(id);
            }
            if(point!=0){
                goodsService.nullCategoryGoodsPoList(id);
                goodsService.deleteGoodsCategory(id);
            }
            retObj=ResponseUtil.ok();
            log.setStatusCode(1);
        }
        logServiceApi.addLog(log);
        return retObj;
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
                            @RequestParam(defaultValue = "5")  Integer limit){
        Object retobj;
        if(page==null||limit==null){
            retobj=ResponseUtil.getBrandListFail();
        }else {
            PageHelper.startPage(page,limit);
            List<BrandPo> brandList = goodsService.listBrand();
            PageInfo<BrandPo> brandPageInfo = new PageInfo<>(brandList);
            List<BrandPo> pagelist =brandPageInfo.getList();
            retobj=ResponseUtil.ok(pagelist);
        }

        return retobj;
    }

        /**
     * 查看所有一级分类
     *
     * @return
     */
    @GetMapping("/categories/l1")
    public Object listOneLevelCategory() {
        Object retobj;
        retobj=ResponseUtil.ok(goodsService.listOneLevelGoodsCategory());
        return retobj;
    }

    /**
     * 获取当前一级分类下的二级分类
     *
     * @param id 分类类目ID
     * @return 当前分类栏目
     */
    @GetMapping("/categories/l1/{id}/l2")
    public Object listSecondLevelGoodsCategoryById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        if(id<=0||id==null){
            retobj=ResponseUtil.nullCategoryFail();
        }else {
            retobj=ResponseUtil.ok(goodsService.listSecondLevelGoodsCategoryById(id));
        }
        return retobj;
    }

    /**
     * 内部接口
     * @param id
     * @return
     */
    @GetMapping("/inner/goods/{id}")
    public GoodsPo getGoodsByIdIn(@PathVariable(value = "id") Integer id) {
        return goodsService.userGetGoodsById(id);

    }


    /**
     * 内部接口
     * @param id
     * @return
     */
    @GetMapping("/products/in/{id}")
    public ProductPo getProductPoById(@PathVariable(value = "id") Integer id) {
        return goodsService.getProductById(id);
    }


    /**
     * 内部接口，判断商品是否在售
     *
     * @param id 商品ID
     * @return
     */
    @GetMapping("/goods/{id}/isOnSale")
    public boolean isOnSale(@PathVariable(value = "id") Integer id){
        GoodsPo goodsPo=goodsService.userGetGoodsById(id);
        if(goodsPo.getStatusCode()==0){
            return false;
        }
        return true;
    }

    /**
     * 根据 orderItemList 来修改库存。operation:true 代表加库存，false 代表减库存
     * @param orderItemList
     * @param operation
     * @return
     * ？？？/？？？？？？？？？？？？？？？？？？？？？？？？？？
     */

    @PutMapping("/product/list/deduct")
    public boolean deduct(@RequestBody List<OrderItem> orderItemList, @RequestParam boolean operation) {
        for (OrderItem orderItem : orderItemList) {
            ProductPo productPo = goodsService.getProductById(orderItem.getProductId());
            Integer stockb = productPo.getSafetyStock();
            if (operation == true) {
                Integer stocka1 = stockb + orderItem.getNumber();
                productPo.setSafetyStock(stocka1);
            } else if (operation == false) {
                Integer stocka2 = stockb - orderItem.getNumber();
                if (stocka2 < 0) {
                    return false;
                }else {
                    productPo.setSafetyStock(stocka2);
                }
            }
        }
        return true;
    }
//        ProductPo productPo=new ProductPo();
//        productPo=goodsService.getProductById(id);
//        Integer stockb=productPo.getSafetyStock();
//        Integer stocka=stockb-quantity;
//        if(stocka<0){
//            return false;
//        }
//        productPo.setSafetyStock(stocka);
//        return true;
//    }
    @GetMapping("/user/product/{id}")
    public Product orderGetProduct(@PathVariable(value = "id") Integer productId){
        Product product=goodsService.getProduct(productId);
        product.setGoodsPo(goodsService.getGoodsById(product.getGoodsId()));
        return product;
    }


}
