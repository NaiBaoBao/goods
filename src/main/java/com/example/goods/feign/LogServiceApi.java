package com.example.goods.feign;

import com.example.goods.domain.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 */
@Component
@FeignClient(value = "logService")
public interface LogServiceApi {
    /**
     * 添加日志
     * @param log
     */
    @PostMapping("/log")
    void addLog(@RequestBody Log log);
}
