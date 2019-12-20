package com.example.goods;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.goods.domain.Brand;
import com.example.goods.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PutBrandsId {
    @Value("http://${host}:${port}/goodsService/brands/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * 标准组样例
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{
        // 准备要更新的数据
        Brand brand = new Brand();
        brand.setId(3);
        brand.setName("wjaaa");
        brand.setGmtModified(LocalDateTime.now());

        // 设置头部，未设置登陆
        URI uri = new URI(url.replace("{id}","3"));
        HttpHeaders headers = testRestTemplate.headForHeaders(uri);
        HttpEntity<Brand> requestUpdate = new HttpEntity<>(brand, headers);

        // 发出http请求
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 取出返回的body
        Brand responseBrand = JacksonUtil.parseObject(response.getBody(), "data", Brand.class);

        // 比较值是否相等
        Integer errno = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(0, errno);
        assertEquals(brand.getId(), responseBrand.getId());
        assertEquals(brand.getName(), responseBrand.getName());
        assertEquals(brand.getGmtModified(), responseBrand.getGmtModified());
    }
}
