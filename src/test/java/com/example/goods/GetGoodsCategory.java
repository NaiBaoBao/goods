package com.example.goods;

import com.example.goods.domain.GoodsCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.goods.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.goods.util.JacksonUtil;
/**
 * @author 24320172203264
 * @version 1.0
 * @date 2019/12/10 20:04
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetGoodsCategory {
    @Value("http://localhost:8017/categories/123")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test12174() throws Exception{
        URI uri = new URI(url.replace("{id}","1005007"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        GoodsCategory goodsCategory = JacksonUtil.parseObject(body,"data",GoodsCategory.class);
        assertEquals(1005007,goodsCategory.getId());
        assertEquals("锅具",goodsCategory.getName());
        assertEquals("http://yanxuan.nosdn.127.net/4aab4598017b5749e3b63309d25e9f6b.png",goodsCategory.getPicUrl());
    }

@Test
    public void test12175() throws Exception
    {
        URI uri = new URI(url.replace("{id}","0"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        String result = responseEntity.getBody();
        String errno = JacksonUtil.parseString(result,"errno");
        String data=JacksonUtil.parseString(result,"data");
        String errmsg = JacksonUtil.parseString(result,"errmsg");

        assertEquals("402",errno);
        assertEquals("参数值不对",errmsg);

    }
}
