package com.example.goods;
/**
 * @author 24320172203264
 * @version 1.0
 * @date 2019/12/10 20:04
 */


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.goods.domain.Goods;
import com.example.goods.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetGoods {
    @Value("http://localhost:8017/goods/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test12171() throws Exception{
        URI uri = new URI(url.replace("{id}","1006019"));
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

        Goods goods = JacksonUtil.parseObject(body,"data",Goods.class);
        assertEquals(1006019,goods.getId());
        assertEquals("大自然的搬运工",goods.getBrief());
        assertEquals("大自然的搬运工",goods.getDescription());
        assertEquals("1127052",goods.getGoodsSn());
        assertEquals("农夫山泉",goods.getName());
        assertEquals("http://yanxuan.nosdn.127.net/4eb09e08ac9de543d2291d27a6be0b54.jpg/",goods.getPicUrl());
    }

 @Test
    public void test12172() throws Exception
    {
        URI uri = new URI(url.replace("{id}","1"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        String result = responseEntity.getBody();
        String errno = JacksonUtil.parseString(result,"errno");
        String data=JacksonUtil.parseString(result,"data");
        String errmsg = JacksonUtil.parseString(result,"errmsg");

        assertEquals("0",errno);
        assertEquals("null",data);
        assertEquals("成功",errmsg);

    }
@Test
    public void test12173() throws Exception
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
