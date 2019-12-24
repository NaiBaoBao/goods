package com.example.goods.feign;

import com.example.goods.domain.GrouponRule;
import com.example.goods.domain.PresaleRule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author
 */
@Component
@FeignClient(value = "DiscountService")
public interface DiscountServiceApi {
    /**
     * 通过goodsId查找团购规则
     * @param id goodsId
     * @return
     */
    @GetMapping(value = "/goods/{id}/grouponRule")
    public GrouponRule getGrouponRuleById(@PathVariable Integer id);
    /**
     *获得对应商品的分享规则
     * @param id 商品id
     * @return
     */
    @GetMapping("/goods/{id}/presaleRule")
    public PresaleRule getPresaleRule(@PathVariable("id") Integer id);
}
