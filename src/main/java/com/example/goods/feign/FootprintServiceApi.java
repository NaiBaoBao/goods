package com.example.goods.feign;


import com.example.goods.domain.FootprintItemPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 */
@FeignClient(value = "footprintService",url = "http://47.98.162.161:8016")
@Service
//@RequestMapping("/footprintService")
public interface FootprintServiceApi {
    /**
     * 调用footprint模块的添加足迹
     * @param footprintItemPo
     * @return
     */
    @PostMapping("/footprints")
    FootprintItemPo addFootprint(@RequestBody FootprintItemPo footprintItemPo);
}
