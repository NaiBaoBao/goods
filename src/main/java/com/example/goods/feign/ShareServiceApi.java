package com.example.goods.feign;

import com.example.goods.domain.ShareRule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author
 */
@Component
@FeignClient(value = "shareRuleService")
public interface ShareServiceApi {
    /**
     * 通过goodsId查找分享规则
     * @param id goodsId
     * @return
     */
    @GetMapping(value = "/shareRule/{id}")
    ShareRule getShareRuleById(@PathVariable Integer id);
}
