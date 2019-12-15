package com.example.goods.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author
 */
@FeignClient(value = "Comment")
@Service
public interface CommentServiceApi {
    /**
     * 调用comment模块的删除足迹
     * @param
     * @return
     */
    @DeleteMapping("/products/{id}/comments")
    int deletecommentbyproduct(@PathVariable Integer id);
}
