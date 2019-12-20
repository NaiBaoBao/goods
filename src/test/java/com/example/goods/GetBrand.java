//package com.example.goods;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.*;
//import org.springframework.test.context.junit4.SpringRunner;
//import com.example.goods.domain.Brand;
//import com.example.goods.util.JacksonUtil;
//
//import java.net.URI;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
////
////<<<<<<<Updated upstream:src/test/java/xmu/oomall/publictest/goods/GetBrand.java
////        package xmu.oomall.publictest.goods;
///**
// * @author 24320172203187
// * @version 1.0
// * @date 2019/12/10 20:04
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class GetBrand {
//    @Value("http://${host}:${port}/brands/{id}")
//    String url;
//
//
///**
// * @author 24320172203187
// * @version 1.0
// * @date 2019/12/10 20:04
// */
//
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class GetBrand {
//    @Value("http://${host}:${port}/brands/{id}")
//    String url;
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Test
//    public void test12168() throws Exception{
//        URI uri = new URI(url.replace("{id}","1001007"));
//        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        /*exchange方法模拟HTTP请求*/
//        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        /*取得响应体*/
//        String body = response.getBody();
//        Integer errno = JacksonUtil.parseInteger(body, "errno");
//        assertEquals(0, errno);
//
//        Brand brand=JacksonUtil.parseObject(body,"data",Brand.class);
//        assertEquals(1001007,brand.getId());
//        assertEquals("优衣库制造商",brand.getName());
//        assertEquals("http://yanxuan.nosdn.127.net/0d72832e37e7e3ea391b519abbbc95a3.png",brand.getPicUrl());
//        assertEquals("严选找到日本知名服装UNIQLO的制造商，\n" +
//                "选取优质长绒棉和精梳工艺，\n" +
//                "与厂方一起设计，为你提供理想的棉袜。",brand.getDescription());
//    }
//    @Test
//    public void test12169() throws Exception
//    {
//        URI uri = new URI(url.replace("{id}","1"));
//        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        String result = responseEntity.getBody();
//        String errno = JacksonUtil.parseString(result,"errno");
//        String data=JacksonUtil.parseString(result,"data");
//        String errmsg = JacksonUtil.parseString(result,"errmsg");
//
//        assertEquals("0",errno);
//        assertEquals("null",data);
//        assertEquals("成功",errmsg);
//
//    }
//
//    @Test
//    public void test12170() throws Exception
//    {
//        URI uri = new URI(url.replace("{id}","0"));
//        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        String result = responseEntity.getBody();
//        String errno = JacksonUtil.parseString(result,"errno");
//        String data=JacksonUtil.parseString(result,"data");
//        String errmsg = JacksonUtil.parseString(result,"errmsg");
//
//        assertEquals("402",errno);
//        assertEquals("参数值不对",errmsg);
//
//    }
//}
//
//
///**
// * @author 24320172203264
// * @version 1.0
// * @date 2019/12/11 23:28
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class DeleteGoodsCategory {
//
//    @Value("http://${host}:${port}/goodsService/categories/{id}")
//
//    String url;
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Test
//
//    public void test12168() throws Exception{
//        URI uri = new URI(url.replace("{id}","1001007"));
//        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        /*exchange方法模拟HTTP请求*/
//        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        /*取得响应体*/
//        String body = response.getBody();
//        Integer errno = JacksonUtil.parseInteger(body, "errno");
//        assertEquals(0, errno);
//
//        Brand brand=JacksonUtil.parseObject(body,"data",Brand.class);
//        assertEquals(1001007,brand.getId());
//        assertEquals("优衣库制造商",brand.getName());
//        assertEquals("http://yanxuan.nosdn.127.net/0d72832e37e7e3ea391b519abbbc95a3.png",brand.getPicUrl());
//        assertEquals("严选找到日本知名服装UNIQLO的制造商，\n" +
//                "选取优质长绒棉和精梳工艺，\n" +
//                "与厂方一起设计，为你提供理想的棉袜。",brand.getDescription());
//    }
//    @Test
//    public void test12169() throws Exception
//    {
//        URI uri = new URI(url.replace("{id}","1"));
//        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
//        String result = responseEntity.getBody();
//        String errno = JacksonUtil.parseString(result,"errno");
//        String data=JacksonUtil.parseString(result,"data");
//        String errmsg = JacksonUtil.parseString(result,"errmsg");
//
//        assertEquals("0",errno);
//        assertEquals("null",data);
//
////    public void test12163() throws Exception
////    {
////        URI uri = new URI(url.replace("{id}","2"));
////
////        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
////        String result = responseEntity.getBody();
////        String errno = JacksonUtil.parseString(result,"errno");
////        String errmsg = JacksonUtil.parseString(result,"errmsg");
////
////        assertEquals("0",errno);
////
////        assertEquals("成功",errmsg);
////
////    }
////
////
////    @Test
////    public void test12170() throws Exception
////
////@Test
////    public void test12164() throws Exception
////
////    {
////        URI uri = new URI(url.replace("{id}","0"));
////        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
////        HttpEntity httpEntity = new HttpEntity(httpHeaders);
////
////        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
////
////        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
////
////        String result = responseEntity.getBody();
////        String errno = JacksonUtil.parseString(result,"errno");
////        String data=JacksonUtil.parseString(result,"data");
////        String errmsg = JacksonUtil.parseString(result,"errmsg");
////
////        assertEquals("402",errno);
////        assertEquals("参数值不对",errmsg);
////
////    }
////
////@Test
////    public void test12165() throws Exception
////    {
////        URI uri = new URI(url.replace("{id}","1"));
////        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
////        HttpEntity httpEntity = new HttpEntity(httpHeaders);
////
////        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
////        String result = responseEntity.getBody();
////        String errno = JacksonUtil.parseString(result,"errno");
////        String data=JacksonUtil.parseString(result,"data");
////        String errmsg = JacksonUtil.parseString(result,"errmsg");
////
////        assertEquals("0",errno);
////        assertEquals("null",data);
////        assertEquals("正确",errmsg);
////
////    }
//}
//
