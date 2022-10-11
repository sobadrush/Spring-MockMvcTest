package com.rogerlo.demo.springmockmvctest.conrtoller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rogerlo.demo.springmockmvctest.model.ProductVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 使用 TestRestRemplate 進行測試時，務必先使用 SpringBoot Main 啟動應用程式
 */
@SpringBootTest
public class ProductControllerTest {

    @BeforeEach
    void beforeEach(TestInfo testInfo){
        System.out.println("=========================【" + testInfo.getDisplayName() + "】===========================");
    }

    @Test
    @DisplayName("測試使用 testRestTemplate 回應 String")
    void test_001() {
        final String uri = "http://localhost:8080/ProductController/getAllProducts";
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate.getForEntity(uri, String.class);
        System.out.println("response = " + response);
    }

    @Test
    @DisplayName("測試使用 ParameterizedTypeReference 達成泛型效果")
    void test_exchange() throws JsonProcessingException {
        final String uri = "http://localhost:8080/ProductController/getAllProducts";
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        List<ProductVO> response = testRestTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductVO>>() {}).getBody();
        System.out.println("response = " + response);
    }

}
