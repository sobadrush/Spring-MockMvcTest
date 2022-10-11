package com.rogerlo.demo.springmockmvctest.conrtoller;

import com.rogerlo.demo.springmockmvctest.model.ProductVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // 代表測試開始時會在元件容器中建立 MockMvc 元件，請讀者注入進來
public class ProductControllerTest_MockMVC {

    @Autowired
    private MockMvc mockMvc;

    // @Autowired // 【Step 1】
    // private WebApplicationContext wac;

    @BeforeEach
    void beforeEach(TestInfo testInfo){
        System.out.println("=========================【" + testInfo.getDisplayName() + "】===========================");
        // mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); // 【Step 2】
    }

    @Test
    @DisplayName("測試使用 MockMVC 發送請求")
    void test_001() throws Exception {
        // System.out.println("mockMvc = " + mockMvc);
        final String uri = "/ProductController/getProduct/102";
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON) // 請求本體
                ) // 發送請求
                .andReturn(); // 取得最後 response
        int status = result.getResponse().getStatus();
        System.out.println("result = " + result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        System.out.println("status = " + status);
    }

    @Test
    @DisplayName("測試使用 MockMVC 發送請求，使用 andExpect 驗證")
    void test_002() throws Exception {

        // 建立 HttpHeader
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // 建立「期望結果」物件
        final ProductVO exceptedVO
                = ProductVO.builder().id(102).name("鍵盤").price(Double.valueOf(55.66)).build();

        // 建立請求物件
        final String uri = "/ProductController/getProduct/102";
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(uri).headers(httpHeaders);

        // 發送請求
        // === Assert Fail ===
        // mockMvc.perform(requestBuilder)
        //         .andDo(print())
        //         .andExpect(status().isOk())
        //         .andExpect(jsonPath("$.id").hasJsonPath())
        //         .andExpect(jsonPath("$.name").value("28吋抗藍光螢幕"))
        //         .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(24999)))
        //         .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        // 發送請求
        // === Assert Pass ===
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(exceptedVO.getName()))
                .andExpect(jsonPath("$.price").value(exceptedVO.getPrice()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    }
}
