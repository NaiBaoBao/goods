package com.example.goods.feign;

import com.example.goods.domain.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**需要改成logService 还有@RequestMapping
 * @author
 */
@Component
@FeignClient(value = "Log")
public interface LogServiceApi {
    @PostMapping("/log")
    void addLog(@RequestBody Log log);
}
