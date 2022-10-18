package com.rogerlo.demo.springmockmvctest.conrtoller;

import com.rogerlo.demo.springmockmvctest.model.UserVO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SpringBoot 單元測試: 用 mockmvc 模擬 session
 * ref. https://www.cnblogs.com/architectforest/p/14583410.html
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MockHttpSessionTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock 的 HttpSession
    private static MockHttpSession MOCK_SESSION;

    private static List<String> UUID_LIST = new ArrayList<>(); // user id list

    @BeforeAll
    public static void setUp() {
        MOCK_SESSION = new MockHttpSession();
        UserVO userVO = new UserVO("Roger Lo", "asdf5566");
        var userUUID = userVO.getUserId();
        MOCK_SESSION.setAttribute(userUUID, userVO);
        UUID_LIST.add(userUUID);
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo) {
        System.err.println(" === " + testInfo.getDisplayName() + " === ");
    }

    @Test
    @DisplayName("測試使用 MockHttpSession 模擬 HttpSession 在 Controller 中動作 - setUserIntoSession")
    public void test_001() throws Exception {

        // 請求參數
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("username", "Kelly");
        multiValueMap.add("password", "30678");

        MvcResult mvcResult = mockMvc.perform(
            post("/UserController/setUserIntoSession")
                    .session(MOCK_SESSION)
                    // .param("username", "Kelly")
                    // .param("password", "30678")
                    .params(multiValueMap) // 請求參數
                )
                //.andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String resultUUID = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("Result : \n\t" + resultUUID);

        String uuidAfterAdd = resultUUID;
        UUID_LIST.add(uuidAfterAdd);
    }

    @Test
    @DisplayName("測試使用 MockHttpSession 模擬 HttpSession 在 Controller 中動作 - getSessionUser")
    public void test_002() {

        UUID_LIST.forEach(uuid -> {
            System.err.println("uuid = " + uuid);
            MvcResult mvcResult;
            try {
                mvcResult = mockMvc.perform(get("/UserController/getSessionUser/" + uuid)
                                        .session(MOCK_SESSION)
                        )
                        //.andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
                System.out.println("Result : \n\t" + mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

}
