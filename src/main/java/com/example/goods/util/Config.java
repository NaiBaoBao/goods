package com.example.goods.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class Config {
    /**
     * redis缓存失效时间，单位分钟
     */
    @Value("${goods.redisExpireTime}")
    private Integer redisExpireTime;

    public Integer getRedisExpireTime() {
        return redisExpireTime;
    }

}
