package com.rogerlo.demo.springmockmvctest.conrtoller;

import com.rogerlo.demo.springmockmvctest.dao.ProductDao;
import com.rogerlo.demo.springmockmvctest.model.ProductVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // 代表測試開始時會在元件容器中建立 MockMvc 元件，請讀者注入進來
public class ProductControllerTest_MockMVC_2 {

    @Autowired
    private MockMvc mockMvc;

    // SpyBean 和 MockBean 生成的對象受 spring 管理，相當於自動替換對應類型 bean 的注入
    @MockBean // 使用 mock 建立物件並自動裝配
    // @SpyBean // 使用 spy 建立物件並自動裝配
    private ProductDao productDao;

    // 設定 預期結果
    private static List<ProductVO> EXPECTED_LIST = Stream.of(
            ProductVO.builder().id(1001).name("滑鼠").price(Double.valueOf(33.5)).build(),
            ProductVO.builder().id(1002).name("鍵盤").price(Double.valueOf(55.66)).build(),
            ProductVO.builder().id(1003).name("耳機").price(Double.valueOf(13)).build()
    ).collect(Collectors.toList());

    @BeforeEach
    void beforeEach(TestInfo testInfo){
        System.out.println("=========================【" + testInfo.getDisplayName() + "】===========================");
    }

    @Test
    @DisplayName("測試使用 @MockBean + MockMVC")
    void test_001() throws Exception {

        // 模擬 productDao.getAllProducts() 回傳預期結果
        Mockito.when(productDao.getAllProducts()).thenReturn(EXPECTED_LIST);
        System.out.println("productDao.getAllProducts() = " + productDao.getAllProducts());

        // 建立請求物件
        final String uri = "/ProductController/getAllProducts";

        // 建立 HttpHeader
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(uri).headers(httpHeaders);

        String contentAsString = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        System.err.println("contentAsString = " + contentAsString);
    }

}
