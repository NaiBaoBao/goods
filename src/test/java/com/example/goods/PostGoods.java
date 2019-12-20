package com.example.goods;

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

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostGoods {
    @Value("http://localhost:8017/goods")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() throws Exception{
        /* 准备数据 */
        Goods good=new Goods();
        good.setId(10086);
        good.setGmtCreate(LocalDateTime.now());
        good.setName("农夫山泉");
        good.setGoodsSn("10089");
        good.setShortName("山泉");
        good.setDescription("大自然的搬运工");
        good.setBrief("可以喝");
        good.setStatusCode(0);
        good.setGoodsCategoryId(1009);
        good.setBrandId(10086);
        good.setWeight(new BigDecimal(100.05));
        good.setVolume("500ml");
        good.setSpecialFreightId(0);
        good.setBeSpecial(false);
        good.setBeDeleted(false);

        /* 设置请求头部 */
        URI uri = new URI(url.replace("{id}", "1"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(good, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /* assert判断 */
        Goods testGoods = JacksonUtil.parseObject(body, "data", Goods.class);
        assertEquals(good, testGoods);

    }
}
