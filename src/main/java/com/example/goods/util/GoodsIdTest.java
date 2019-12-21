//package com.example.goods.util;
///**
// * @author 24320172203264
// * @version 1.0
// * @date 2019/12/10 20:04
// */
//
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//import com.example.goods.PublicTestApplication;
//import xmu.oomall.domain.Goods;
//import xmu.oomall.publictest.UserAccount;
//
//import java.net.URI;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(classes = PublicTestApplication.class)
//public class GoodsIdTest {
//    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/goods/{id}")
//    String url;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
////    @Autowired
////    private UserAccount userAccount;
//
//    /**
//     * @author Ming Qiu
//     * @throws Exception
//     */
//    @Test
//    public void tc_GoodsId_001() throws Exception{
//        URI uri = new URI(url.replace("{id}","288"));
//        HttpHeaders httpHeaders = restTemplate.createHeaders();
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        /*exchange方法模拟HTTP请求*/
//        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        /*取得响应体*/
//        String body = response.getBody();
//        Integer errno = JacksonUtil.parseInteger(body, "errno");
//        assertEquals(0, errno);
//
//        Goods goods =JacksonUtil.parseObject(body,"data",Goods.class);
//        assertEquals(374,goods.getId());
//        assertEquals("drh-szd0002",goods.getGoodsSn());
//        assertEquals("深圳客户专享-古彩•白蛇传瓷瓶",goods.getName());
//    }
//
//    /**
//     * @author Ming Qiu
//     * @throws Exception
//     */
//    @Test
//    public void tc_GoodsId_002() throws Exception{
//        URI uri = new URI(url.replace("{id}","10000223"));
//        HttpHeaders httpHeaders = restTemplate.headForHeaders(uri);
//        //createHeaders();
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        /*exchange方法模拟HTTP请求*/
//        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        /*取得响应体*/
//        String body = response.getBody();
//        Integer errno = JacksonUtil.parseInteger(body, "errno");
//        assertEquals(775, errno); //该商品不存在
//
//    }
//
//
//}
