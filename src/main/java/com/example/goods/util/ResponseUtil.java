package com.example.goods.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应操作结果
 * <pre>
 *  {
 *      errno： 错误码，
 *      errmsg：错误消息，
 *      data：  响应数据
 *  }
 * </pre>
 *
 * <p>
 * 错误码：
 * <ul>
 * <li> 0，成功；
 * <li> 4xx，前端错误，说明前端开发者需要重新了解后端接口使用规范：
 * <ul>
 * <li> 401，参数错误，即前端没有传递后端需要的参数；
 * <li> 402，参数值错误，即前端传递的参数值不符合后端接收范围。
 * </ul>
 * <li> 5xx，后端错误，除501外，说明后端开发者应该继续优化代码，尽量避免返回后端错误码：
 * <ul>
 * <li> 501，验证失败，即后端要求用户登录；
 * <li> 502，系统内部错误，即没有合适命名的后端内部错误；
 * <li> 503，业务不支持，即后端虽然定义了接口，但是还没有实现功能；
 * <li> 504，更新数据失效，即后端采用了乐观锁更新，而并发更新时存在数据更新失效；
 * <li> 505，更新数据失败，即后端数据库更新失败（正常情况应该更新成功）。
 * </ul>
 * <li> 6xx，小商城后端业务错误码，
 * 具体见litemall-admin-api模块的AdminResponseCode。
 * <li> 7xx，管理后台后端业务错误码，
 * 具体见litemall-wx-api模块的WxResponseCode。
 * </ul>
 */
/**
 * @author
 */
public class ResponseUtil {
    public static Object ok() {
        Map<String, Object> obj = new HashMap<String, Object>(1000);
        obj.put("errno", 0);
        obj.put("errmsg", "成功");
        return obj;
    }

    public static Object ok(Object data) {
        Map<String, Object> obj = new HashMap<String, Object>(1000);
        obj.put("errno", 0);
        obj.put("errmsg", "成功");
        obj.put("data", data);
        return obj;
    }

    public static Object fail() {
        Map<String, Object> obj = new HashMap<String, Object>(1000);
        obj.put("errno", -1);
        obj.put("errmsg", "错误");
        return obj;
    }

    public static Object fail(int errno, String errmsg) {
        Map<String, Object> obj = new HashMap<String, Object>(1000);
        obj.put("errno", errno);
        obj.put("errmsg", errmsg);
        return obj;
    }

    public static Object badArgument() {
        return fail(401, "参数类型不对");
    }

    public static Object badArgumentValue() {
        return fail(402, "参数值不对");
    }

    public static Object unlogin() {
        return fail(501, "请登录");
    }

    public static Object serious() {
        return fail(502, "系统内部错误");
    }

    public static Object unsupport() {
        return fail(503, "业务不支持");
    }

    public static Object updatedDateExpired() {
        return fail(504, "更新数据已经失效");
    }

    public static Object updatedDataFailed() {
        return fail(505, "更新数据失败");
    }

    public static Object unauthz() {
        return fail(506, "无操作权限");
    }

    public static Object paramfail(){
        return fail(580,"非法操作");
    }

    public static Object illegel(){
        return fail(507,"非法操作");
    }

    public static Object addGoodsFail(){return fail(771,"商品新增失败");}

    public static Object updateGoodsFail(){return fail(772,"商品修改失败");}

    public static Object deleteGoodsFail(){return fail(773,"商品删除失败");}

    public static Object xiajiaGoodsFail(){return fail(774,"商品下架失败");}

    public static Object nullGoodsFail(){return fail(775,"该商品不存在");}

    public static Object getGoodsListFail(){return fail(776,"获取商品列表失败");}

    public static Object addProductFail(){return fail(781,"产品新增失败");}

    public static Object updateProductFail(){return fail(782,"产品修改失败");}

    public static Object deleteProductFail(){return fail(783,"产品删除失败");}

    public static Object nullProductFail(){return fail(784,"该产品不存在");}

    public static Object getProductListFail(){return fail(785,"获取产品列表失败");}

    public static Object updateProductStockFail(){return fail(786,"修改产品库存失败");}

    public static Object addBrandFail(){return fail(791,"品牌新增失败");}

    public static Object updateBrandFail(){return fail(792,"品牌修改失败");}

    public static Object deleteBrandFail(){return fail(793,"品牌删除失败");}

    public static Object nullBrandFail(){return fail(794,"该品牌不存在");}

    public static Object getBrandListFail(){return fail(795,"获取品牌列表失败");}

    public static Object addCategoryFail(){return fail(801,"分类新增失败");}

    public static Object updateCategoryFail(){return fail(802,"分类修改失败");}

    public static Object deleteCategoryFail(){return fail(803,"分类删除失败");}

    public static Object nullCategoryFail(){return fail(804,"该分类不存在");}

    public static Object getCategoryListFail(){return fail(805,"获取分类列表失败");}



}


