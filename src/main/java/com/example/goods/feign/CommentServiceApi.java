package com.example.goods.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 */
@FeignClient(value = "commentService")
@Service
@RequestMapping("/commentService")
public interface CommentServiceApi {
    /**
     * 调用comment模块的删除足迹
     * @param
     * @return
     */
    @DeleteMapping("/products/{id}/comments")
    String deletecommentbyproduct(@PathVariable Integer id);
}
