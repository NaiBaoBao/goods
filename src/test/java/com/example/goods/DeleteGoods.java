package com.example.goods;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.goods.util.JacksonUtil;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteGoods {

    @Value("http://${host}:${port}/goods/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test12165() throws Exception
    {
        URI uri = new URI(url.replace("{id}","1006002"));

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
        String result = responseEntity.getBody();
        String errno = JacksonUtil.parseString(result,"errno");
        String errmsg = JacksonUtil.parseString(result,"errmsg");

        assertEquals("0",errno);
        assertEquals("成功",errmsg);

    }
 @Test
    public void test12167() throws Exception
    {
        URI uri = new URI(url.replace("{id}","0"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        String result = responseEntity.getBody();
        String errno = JacksonUtil.parseString(result,"errno");
        String data=JacksonUtil.parseString(result,"data");
        String errmsg = JacksonUtil.parseString(result,"errmsg");

        assertEquals("402",errno);
        assertEquals("参数值不对",errmsg);

    }
}
