package com.rogerlo.demo.springmockmvctest.conrtoller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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
    @DisplayName("測試使用 ParameterizedTypeReference 達成泛型效果")
    void test_exchange() throws JsonProcessingException {
        final String urlTemplate = "http://localhost:%d/ProductController/getAllProducts";
        final String api_url = String.format(urlTemplate, randomServerPort);
        List<ProductVO> response = this.testRestTemplate.exchange(api_url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductVO>>() {}).getBody();
        System.out.println("response = " + response);
    }

}
