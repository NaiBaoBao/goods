package com.example.goods.controller;

import com.example.goods.domain.*;
import com.example.goods.feign.*;

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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.goods.util.ResponseUtil.fail;

/**
 * @author
 */
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    FootprintServiceApi footprintServiceApi;
    @Autowired
    CommentServiceApi commentServiceApi;
    @Autowired
    ShareServiceApi shareServiceApi;
    @Autowired
    DiscountServiceApi discountServiceApi;
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
     * 新建商品，不能建在一级分类下面，更改同理, 新建是下架状态
     *
     * @param goodsPo
     * @return GoodsPo，即新建的一个商品
     */
    @PostMapping("/goods")
    public Object addGoods(@RequestBody GoodsPo goodsPo) {
        Object retobj=new Object();
        Log log=new Log();
        log.setActions("新建商品");
        log.setType(1);
        GoodsPo goodsPo1=goodsService.addGoods(goodsPo);
        Integer cid=goodsPo.getGoodsCategoryId();
        Integer id;
        if(cid==null){
            if(goodsPo1!=null) {
                retobj=ResponseUtil.ok(goodsPo1);
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.addGoodsFail();
                log.setStatusCode(0);
            }
        }else {
            GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(cid);
            if(goodsCategoryPo.getPid()==0) {
                retobj=ResponseUtil.paramfail();
                log.setStatusCode(0);
            }
            else if(goodsCategoryPo.getPid()!=0){
                if(goodsPo1!=null) {
                    id=goodsPo.getId();
                    goodsPo=goodsService.getGoodsById(id);
                    retobj=ResponseUtil.ok(goodsPo);
                    log.setStatusCode(1);
                }else {
                   retobj=ResponseUtil.addGoodsFail();

                    log.setStatusCode(0);
                }
            }
        }
        logServiceApi.addLog(log);
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
        if(id==null||id<0){
            log.setStatusCode(0);
            retObi=ResponseUtil.paramfail();
        }else if(goodsPo==null){
            retObi=ResponseUtil.nullGoodsFail();
            log.setStatusCode(0);
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
     * 根据id更新商品信息，不能更改在二级分类下
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
        goodsPo.setId(id);
        GoodsPo goodsPo1=goodsService.updateGoodsById(goodsPo);
        if(id==null||id<0){
            log.setStatusCode(0);
            retobj=ResponseUtil.paramfail();
        }else if(goodsPo==null||goodsPo1==null){
            retobj=ResponseUtil.nullGoodsFail();
            log.setStatusCode(0);
        }else {
                Integer cid=goodsPo1.getGoodsCategoryId();
                if(cid==0){
                    if(goodsPo1!=null){
                        retobj=ResponseUtil.ok(goodsPo1);
                    log.setStatusCode(1);
                    }else {
                        retobj=ResponseUtil.updateGoodsFail();
                    log.setStatusCode(0);
                    }
                }else if(cid!=0){
                    GoodsCategoryPo goodsCategoryPo=goodsService.getGoodsCategoryPoById(cid);
                    if(goodsCategoryPo.getPid()==0) {
                        retobj=ResponseUtil.paramfail();
                    log.setStatusCode(0);
                    }
                    else if(goodsCategoryPo.getPid()!=0){
                        if(goodsPo1!=null) {
                            retobj=ResponseUtil.ok(goodsPo1);
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
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit){
        Object retobj;
        Log log=new Log();
        log.setActions("条件搜索商品");
        log.setType(0);
        if(page==null||limit==null||page<=0||limit<=0){
            retobj=ResponseUtil.paramfail();
            log.setStatusCode(0);
        }else {
            PageHelper.startPage(page, limit);
            List<GoodsPo> goodsList = goodsService.adminListGoods(goodsSn, name);
            PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
            List<GoodsPo> pagelist = goodsPageInfo.getList();
            retobj = ResponseUtil.ok(pagelist);
            log.setStatusCode(1);
        }
        logServiceApi.addLog(log);
        return retobj;
    }


    /**
     * 管理员根据id获取某个商品详情
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

        if (id <0||id==null) {
            retobj = ResponseUtil.paramfail();
            log.setStatusCode(0);
        }else {
            Goods goods=goodsService.getGoods(id);
            if(goods==null){
                retobj=ResponseUtil.nullGoodsFail();
            log.setStatusCode(0);
            }else {
                goods.setBrandPo(brandMapper.getBrandPoById(goods.getBrandId()));
                goods.setGoodsCategoryPo(goodsCategoryMapper.getGoodsCategoryPoById(goods.getGoodsCategoryId()));
                goods.setProductPoList(productMapper.listProductByGoodsId(goods.getId()));
                goods.setShareRule(shareServiceApi.getShareRuleById(goods.getId()));
                goods.setGrouponRule(discountServiceApi.getGrouponRuleById(goods.getId()));
                goods.setPresaleRule(discountServiceApi.getPresaleRule(goods.getId()));
                log.setStatusCode(1);
                retobj = ResponseUtil.ok(goods);
            }
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
        if(id<0||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.paramfail();
        }else if(goodsPo==null){
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
     * 管理员添加商品下的产品
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
        if(id<0||goodsPo==null||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.paramfail();
        }else if(goodsPo==null){
            retobj=ResponseUtil.nullGoodsFail();
             log.setStatusCode(0);
        }else {
            productPo.setGoodsId(id);
            ProductPo productPo1=goodsService.addProductByGoodsId(productPo);
            if(productPo1!=null){
                retobj=ResponseUtil.ok(productPo1);
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.addProductFail();
                log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

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
        if(id<0||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.paramfail();
        }else if(productPo==null){
              log.setStatusCode(0);
            retobj=ResponseUtil.nullGoodsFail();
        }else {
            productPo.setId(id);
            ProductPo productPo1=goodsService.updateProductById(productPo);
            if(productPo1!=null){
                retobj=ResponseUtil.ok(productPo1);
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
        if(id<0||id==null){
            log.setStatusCode(0);
            retObi=ResponseUtil.paramfail();
        }else if (productPo==null){
            log.setStatusCode(0);
            retObi=ResponseUtil.nullProductFail();
        }else
            {
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
     * 
     */
    @GetMapping("/goods/{id}")
    public Object userGetGoodsById(HttpServletRequest request,@PathVariable(value = "id") Integer id) {
       Object retobj=new Object();

        String userId = request.getHeader("id");
        Integer i=null;
        if(id<0||id==null){
            retobj=ResponseUtil.paramfail();
        }else {
            Goods goods = goodsService.userGetGoods(id);
            if (goods == null) {
                retobj = ResponseUtil.nullGoodsFail();
            } else {
                FootprintItemPo footprintItemPo = new FootprintItemPo();
                i = Integer.valueOf(userId);
                footprintItemPo.setUserId(i);
                footprintItemPo.setGoodsId(id);
                footprintServiceApi.addFootprint(footprintItemPo);
                goods.setBrandPo(goodsService.getBrandPoById(goods.getBrandId()));
                goods.setGoodsCategoryPo(goodsService.getGoodsCategoryPoById(goods.getGoodsCategoryId()));
                goods.setProductPoList(goodsService.listProductByGoodsId(goods.getId()));
                goods.setShareRule(shareServiceApi.getShareRuleById(goods.getId()));
                goods.setGrouponRule(discountServiceApi.getGrouponRuleById(goods.getId()));
                goods.setPresaleRule(discountServiceApi.getPresaleRule(goods.getId()));
                retobj = ResponseUtil.ok(goods);
            }
        }
        return retobj;
    }

    /**
     * 用户根据品牌id查询商品,必须上架状态
     *
     * @param id
     * @return List<GoodsVo>
     */
    @GetMapping("/brands/{id}/goods")
    public Object userGetBrandsInfoById(@PathVariable(value = "id")Integer id){
        Object retobj;
        BrandPo brandPo=goodsService.getBrandPoById(id);
        brandPo.setId(id);
        if(id<0||id==null){
            retobj=ResponseUtil.paramfail();
        }else if(brandPo==null){
            retobj=ResponseUtil.nullBrandFail();
        }else {
            {
                retobj=ResponseUtil.ok(goodsService.userGetBrandsInfoById(id));
            }
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
        if(id<0||id==null){
            retobj=ResponseUtil.paramfail();
        }else if(goodsCategoryPo==null){
            retobj=ResponseUtil.nullCategoryFail();
        }else {
                Integer pid=goodsCategoryPo.getPid();
                if(pid==0){
                    retobj=ResponseUtil.paramfail();
                }
                else if(pid!=0){
                    PageHelper.startPage(page,limit);
                    List<GoodsPo> goodsList = goodsService.getCategoriesInfoById(id);
                    PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
                    List<GoodsPo> pagelist =goodsPageInfo.getList();
                    retobj=ResponseUtil.ok(pagelist);
                }
            }

        return retobj;
    }
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
                                 @RequestParam(defaultValue = "10") Integer limit){
        Object retobj;
        if(name==null||page==null||limit==null||page<=0||limit<=0){
            retobj=ResponseUtil.paramfail();
        }else {
            PageHelper.startPage(page,limit);
            List<GoodsPo> goodsList = goodsService.listGoods(name);
            PageInfo<GoodsPo> goodsPageInfo = new PageInfo<>(goodsList);
            List<GoodsPo> pagelist =goodsPageInfo.getList();
            if(pagelist==null){
                retobj=ResponseUtil.getGoodsListFail();
            }else {
                retobj=ResponseUtil.ok(pagelist);
            }
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
        if(id<0||page==null||limit==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.nullBrandFail();
        }
        else {
            PageHelper.startPage(page,limit);
            List<BrandPo> brandList = goodsService.listBrandByCondition(id,name);
            PageInfo<BrandPo> brandPageInfo = new PageInfo<>(brandList);
            List<BrandPo> pagelist =brandPageInfo.getList();
            if(pagelist==null){
                retobj=ResponseUtil.getBrandListFail();
             log.setStatusCode(0);
            }else {
                retobj=ResponseUtil.ok(pagelist);
            log.setStatusCode(1);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }


    /**
     * 创建一个品牌
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
        BrandPo brandPo1=goodsService.addBrand(brandPo);
        if(brandPo1==null){
            retobj=ResponseUtil.addBrandFail();
            log.setStatusCode(0);
        }else {
            if (brandPo1!=null) {
                retobj = ResponseUtil.ok(brandPo1);
            log.setStatusCode(1);
            } else {
                retobj = ResponseUtil.addBrandFail();
            log.setStatusCode(0);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     * 管理员查看品牌详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/admin/brands/{id}",produces = "application/json; charset=utf-8")
    public Object adminGetBrandById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        Log log=new Log();
        log.setActions("查看品牌");
        log.setType(0);

        if(id<0||id==null) {
            log.setStatusCode(0);
            retobj = ResponseUtil.paramfail();
        }else {
            BrandPo brandPo = goodsService.getBrandPoById(id);
            if (brandPo == null) {
                retobj = ResponseUtil.nullBrandFail();
                log.setStatusCode(0);
            } else {
                log.setStatusCode(1);
                retobj = ResponseUtil.ok(brandPo);
            }
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
    @GetMapping(value = "/brands/{id}",produces = "application/json; charset=utf-8")
    public Object userGetBrandPoById(@PathVariable(value = "id") Integer id) {
        Object retobj;
        if(id<0||id==null){
            retobj=ResponseUtil.paramfail();
        }else {
            BrandPo brandPo=goodsService.getBrandPoById(id);
            if(brandPo==null){
                retobj=ResponseUtil.nullBrandFail();
            }else {
                retobj=ResponseUtil.ok(brandPo);
            }
        }
        return retobj;
    }

    /**
     * 修改单个品牌的信息
     *
     * @param id
     * @param brandPo
     * @return
     */
    @PutMapping(value = "/brands/{id}",produces = "application/json; charset=utf-8" )
    public Object updateBrandById(@PathVariable(value = "id") Integer id,@RequestBody BrandPo brandPo) {
        Object retobj;
        Log log=new Log();
        log.setActions("修改品牌");
        log.setType(2);
        if(id<0||id==null){
            log.setStatusCode(0);
            retobj=ResponseUtil.paramfail();
        }else if(brandPo==null) {
            log.setStatusCode(0);
            retobj=ResponseUtil.nullBrandFail();
        }else {
                brandPo.setId(id);
                BrandPo brandPo1=goodsService.updateBrandById(brandPo);
                if(brandPo1!=null){
                    retobj=ResponseUtil.ok(brandPo1);
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
     * 删除一个品牌，级联更改商品
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
        if(id<0||id==null){
            log.setStatusCode(0);
            retObi=ResponseUtil.paramfail();
        }else {
            BrandPo brandPo=goodsService.getBrandPoById(id);
            if(brandPo==null){
            log.setStatusCode(0);
                retObi=ResponseUtil.nullBrandFail();
            }else {
                goodsService.nullBrandGoodsPoList(id);
                goodsService.deleteBrandById(id);
                retObi = ResponseUtil.ok();
                retObi = ResponseUtil.deleteBrandFail();
                log.setStatusCode(1);
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
        if(page==null||limit==null||page<=0||limit<=0){
            retobj=ResponseUtil.paramfail();
        }else {
            PageHelper.startPage(page,limit);
            List<GoodsCategoryPo> categoryPoList = goodsService.listGoodsCategory();
            PageInfo<GoodsCategoryPo> categoryPageInfo = new PageInfo<>(categoryPoList);
            List<GoodsCategoryPo> pagelist =categoryPageInfo.getList();
            log.setStatusCode(1);
            if(pagelist==null){
                retobj=ResponseUtil.getCategoryListFail();
            log.setStatusCode(0);
            }else {
                retobj=ResponseUtil.ok(pagelist);
            log.setStatusCode(1);
            }
        }
        logServiceApi.addLog(log);
        return retobj;
    }

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
        if(id<0||id==null){
            retobj=ResponseUtil.paramfail();
            log.setStatusCode(0);
        }else if(goodsCategoryPo==null){
            retobj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        }else {
                retobj=ResponseUtil.ok(goodsCategoryPo);
            log.setStatusCode(1);
            }
        logServiceApi.addLog(log);
        return retobj;
    }

    /**
     * 新建一个分类，不能新建在二级分类下
     * @param goodsCategoryPo
     * @return
     */
    @PostMapping("/categories")
    public Object addGoodsCategory(@RequestBody GoodsCategoryPo goodsCategoryPo) {
        Object retobj = null;
        Log log=new Log();
        log.setActions("新建分类");
        log.setType(1);
        GoodsCategoryPo goodsCategoryPo1=goodsService.addGoodsCategory(goodsCategoryPo);
        Integer pid=goodsCategoryPo.getPid();
        if(pid==null){
            if(goodsCategoryPo1!=null){
                retobj=ResponseUtil.ok(goodsCategoryPo1);
                log.setStatusCode(1);
            }else {
                retobj=ResponseUtil.addCategoryFail();
                log.setStatusCode(0);
            }
        }
        else if(pid!=null){
            GoodsCategoryPo goodsCategoryPo2;
            goodsCategoryPo2=goodsService.getGoodsCategoryPoById(pid);
            if(goodsCategoryPo2.getPid()!=0){
                retobj=ResponseUtil.paramfail();
                log.setStatusCode(0);
            }
            else {
                if(goodsCategoryPo1!=null){
                    retobj=ResponseUtil.ok(goodsCategoryPo1);
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
        goodsCategoryPo.setId(id);
        GoodsCategoryPo goodsCategoryPo1=goodsService.updateGoodsCategoryById(goodsCategoryPo);
        if(id<0||id==null){
            retobj=ResponseUtil.paramfail();
            log.setStatusCode(0);
        }else if(goodsCategoryPo1==null){
            retobj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        }else
            {
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
        goodsCategoryPo.setId(id);
        GoodsCategoryPo goodsCategoryPo1=goodsService.updateGoodsCategoryPid(goodsCategoryPo);
        Integer oldPid=goodsCategoryPo.getPid();
        Integer newPid=goodsCategoryPo1.getPid();
        if(id<0||oldPid<0||id==null||newPid==null){
            retobj=ResponseUtil.paramfail();
            log.setStatusCode(0);
        }else if(goodsCategoryPo==null){
            retobj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        }else {
                GoodsCategoryPo goodsCategoryPo2=goodsService.getGoodsCategoryPoById(newPid);
                if(goodsCategoryPo2.getPid()==0){
                    if(goodsCategoryPo1!=null){
                        retobj=ResponseUtil.ok(goodsCategoryPo1);
                        log.setStatusCode(1);
                    }else {
                        log.setStatusCode(0);
                        retobj=ResponseUtil.updateCategoryFail();
                    }
                }else {
                    retobj=ResponseUtil.paramfail();
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
        if(id<0||id==null){
            retObj=ResponseUtil.paramfail();
            log.setStatusCode(0);
        }else if(goodsCategoryPo==null) {
            retObj=ResponseUtil.nullCategoryFail();
            log.setStatusCode(0);
        } else {
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
                            @RequestParam(defaultValue = "10")  Integer limit){
        Object retobj;
        if(page==null||limit==null||page<=0||limit<=0){
            retobj=ResponseUtil.paramfail();
        }else {
            PageHelper.startPage(page,limit);
            List<BrandPo> brandList = goodsService.listBrand();
            PageInfo<BrandPo> brandPageInfo = new PageInfo<>(brandList);
            List<BrandPo> pagelist =brandPageInfo.getList();
            if(pagelist==null){
                retobj=ResponseUtil.getBrandListFail();
            }else {
                retobj=ResponseUtil.ok(pagelist);
            }
        }
        return retobj;
    }

        /**
     * 查看所有一级分类，内部接口
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
            retobj=ResponseUtil.paramfail();
        }else {
            List<GoodsCategoryPo> goodsCategoryPoList=goodsService.listSecondLevelGoodsCategoryById(id);
            if(goodsCategoryPoList==null){
                retobj=ResponseUtil.getCategoryListFail();
            }else {
                retobj=ResponseUtil.ok(goodsCategoryPoList);
            }
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
     *
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

    /**
     * 内部接口
     * @param productId
     * @return
     */
    @GetMapping("/user/product/{id}")
    public Product orderGetProduct(@PathVariable(value = "id") Integer productId){
        Product product=goodsService.getProduct(productId);
        Integer goodsId=product.getGoodsId();
        Goods goods=goodsService.getGoods(goodsId);
        goods.setBrandPo(brandMapper.getBrandPoById(goods.getBrandId()));
        goods.setGoodsCategoryPo(goodsCategoryMapper.getGoodsCategoryPoById(goods.getGoodsCategoryId()));
        goods.setProductPoList(productMapper.listProductByGoodsId(goods.getId()));
        product.setGoods(goods);
        return product;
    }


}
