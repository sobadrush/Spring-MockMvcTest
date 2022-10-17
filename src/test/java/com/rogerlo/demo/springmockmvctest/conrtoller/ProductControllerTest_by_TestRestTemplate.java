package com.rogerlo.demo.springmockmvctest.conrtoller;

import com.rogerlo.demo.springmockmvctest.model.ProductVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;

/**
 * 使用 TestRestRemplate 進行測試時，務必先使用 SpringBoot Main 啟動應用程式
 * 但若有設定 WebEnvironment.RANDOM_PORT，則可不用啟動 SpringBoot Main 啟動應用程式
 *
 * ref. https://howtodoinjava.com/spring-boot2/testing/testresttemplate-post-example/
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest_by_TestRestTemplate {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    @BeforeEach
    void beforeEach(TestInfo testInfo){
        System.out.println("=========================【" + testInfo.getDisplayName() + "】===========================");
        System.err.println("randomServerPort = " + randomServerPort);
        // For 解決： Invalid HTTP method: PATCH
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    @DisplayName("測試使用 testRestTemplate 回應 String")
    void test_001() {
        final String urlTemplate = "http://localhost:{0}/ProductController/getAllProducts";
        final String api_url = MessageFormat.format(urlTemplate, String.valueOf(randomServerPort));
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(api_url, String.class);
        System.out.println("response = " + response);
    }

    @Test
    @DisplayName("測試使用 testRestTemplate 新增 Product")
    void test_002() throws URISyntaxException {
        // final String urlTemplate = "http://localhost:{0}/ProductController/addProduct";
        final String urlTemplate = "/ProductController/addProduct"; // 可以不用加 IP:Port
        final String api_url = MessageFormat.format(urlTemplate, String.valueOf(randomServerPort));
        URI uri = new URI(api_url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        ProductVO productVOAdd = ProductVO.builder().id(777).name("PS5").price(35000).build();
        HttpEntity<ProductVO> request = new HttpEntity<>(productVOAdd, headers);

        ResponseEntity<ProductVO> resp = this.testRestTemplate.postForEntity(uri, request, ProductVO.class);
        System.out.println("---");
        System.out.println(MessageFormat.format("<<< Http Status: {0}，Response Body: {1}", resp.getStatusCode(), resp.getBody()));
        System.out.println("---");
    }

    @Test
    @DisplayName("測試使用 testRestTemplate 進行 Patch")
    void test_003() throws URISyntaxException {

        int targetProdId = 103; // 要修改的 target Id

        final String urlTemplate = "http://localhost:{0}/ProductController/patchProduct/{1}";
        final String api_url = MessageFormat.format(urlTemplate, String.valueOf(randomServerPort), targetProdId);
        URI uri = new URI(api_url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        ProductVO patchVO = ProductVO.builder().id(targetProdId).name("AppleTV 4").build();
        HttpEntity<ProductVO> request = new HttpEntity<>(patchVO, headers);

        String resp = this.testRestTemplate.patchForObject(uri, request, String.class);
        System.out.println("---");
        System.out.println(MessageFormat.format("<<< response: {0}", resp));
        System.out.println("---");
        // 查詢Patch後結果
        String getAllUrl = "http://localhost:" + randomServerPort + "/ProductController/getAllProducts";
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(getAllUrl, String.class);
        System.out.println("response = " + response);
        System.out.println("---");
    }

    @Test
    @DisplayName("測試使用 ParameterizedTypeReference 達成泛型效果") // ref. https://blog.csdn.net/shanshan_blog/article/details/72770059
    void test_exchange() {
        final String urlTemplate = "http://localhost:%d/ProductController/getAllProducts";
        final String api_url = String.format(urlTemplate, randomServerPort);
        List<ProductVO> response = this.testRestTemplate.exchange(api_url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductVO>>() {}).getBody();
        System.out.println("response = " + response);
    }

}
