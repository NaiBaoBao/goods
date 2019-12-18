package com.example.goods.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.goods.configue.RedisConfig;
import com.example.goods.domain.GoodsPo;
import com.example.goods.domain.ProductPo;
import com.example.goods.mapper.GoodsMapper;
import com.example.goods.mapper.ProductMapper;
import com.example.goods.util.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 */

@Repository
public class RedisDao {

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private ProductMapper productMapper;
    @Autowired
    private Config config;

//    /**
//     * 用ID获取货品
//     * @param id 货品id
//     * @return
//     */
//    public ProductPo getProductById(Integer id){
//        String key = "P_"+id;
//        ProductPo productPo = (ProductPo) redisTemplate.opsForValue().get(key);
//        if (productPo == null) {
//            productPo = productMapper.getProductById(id);
//            System.out.println("redis中没有此货品");
//            redisTemplate.opsForValue().set(key, productPo,config.getRedisExpireTime(), TimeUnit.MINUTES);
//        }
//        return productPo;
//    }

    /**
     * 用ID获取商品
     * @param id 商品id
     * @return 商品
     */
    public GoodsPo getGoodsById(Integer id){
        String key = "G_"+id;
        GoodsPo goodsPo = (GoodsPo) redisTemplate.opsForValue().get(key);
        if (goodsPo == null) {
            goodsPo = goodsMapper.getGoodsById(id);
            System.out.println("redis中没有此商品");
            redisTemplate.opsForValue().set(key, goodsPo, config.getRedisExpireTime(), TimeUnit.MINUTES);
        }
        return goodsPo;
    }
    public GoodsPo userGetGoodsById(Integer id){
        String key = "G_"+id;
        GoodsPo goodsPo = (GoodsPo) redisTemplate.opsForValue().get(key);
        if (goodsPo == null) {
            goodsPo = goodsMapper.getGoodsById(id);
            System.out.println("redis中没有此商品");
            redisTemplate.opsForValue().set(key, goodsPo, config.getRedisExpireTime(), TimeUnit.MINUTES);
        }
        return goodsPo;
    }
}
