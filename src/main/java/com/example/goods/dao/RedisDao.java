package com.example.goods.dao;

import com.example.goods.domain.GoodsPo;
import com.example.goods.mapper.GoodsMapper;
import com.example.goods.mapper.ProductMapper;
import com.example.goods.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyidi
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
