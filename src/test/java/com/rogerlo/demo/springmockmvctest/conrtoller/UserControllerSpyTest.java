package com.rogerlo.demo.springmockmvctest.conrtoller;

import com.rogerlo.demo.springmockmvctest.controller.UserController;
import com.rogerlo.demo.springmockmvctest.dao.UserDao;
import com.rogerlo.demo.springmockmvctest.model.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerSpyTest {

    // @Autowired
    // @SpyBean // 除 spy 外，也會加入到 spring 容器中，可進行裝配
    // @MockBean // 除 mock 外，也會加入到 spring 容器中，可進行裝配
    @Spy
    // @Mock
    @InjectMocks // 表示 UserController 中有屬性會用標註 @Spy/@Mock 的物件注入
    private UserController userController;

    // @Autowired
    // @SpyBean // 除 spy 外，也會加入到 spring 容器中，可進行裝配
    // @MockBean // 除 mock 外，也會加入到 spring 容器中，可進行裝配
    // @Spy
    @Mock
    private UserDao userDao;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("=========================【" + testInfo.getDisplayName() + "】===========================");
    }

    @Test
    @Disabled
    @DisplayName("Spy vs Mock，兩種設定 return 方式測試")
    void test_001() {
        Mockito.doReturn("GGGGG").when(userController).maskPassword(anyString()); // Spy 用此寫法
        // Mockito.when(userController.maskPassword(anyString())).thenReturn("HHH"); // Mock 用此寫法
        System.err.println(userController.maskPassword("ABCDEFGHIJK"));
    }

    @Test
    @Disabled
    @DisplayName("單獨測試 userDao.getUserById()")
    void test_002() {
        UserVO userVO = userDao.getUserById("9576655d-6c9a-4eae-8b54-e6e7a5895fe6");
        System.out.println("userVO = " + userVO);
    }

    @Test
    @DisplayName("測試 @Spy @SpyBean @Mock @Mock @Autowired 的排列組合")
    void test_003() {
        UserVO fakeUserVO = UserVO.builder().userId("1111-2222-3333-4444").username("湯普森").password("nnn").build();

        // Mock userDao
        Mockito.doReturn(fakeUserVO).when(userDao).getUserById(anyString()); // Spy 用此寫法
        // Mockito.when(userDao.getUserById(anyString())).thenReturn(fakeUserVO); // Mock 用此寫法

        // 1. 對 userController 標註 @InjectMocks，僅需設定 userDao 回傳值，“不”需額外設定 userController 層的回傳值
        // 2. 若用 @MockBean，需對最外層的 userController 的 function 設定假的回傳值
        // Mockito.when(userController.getUserById(anyString())).thenReturn(fakeUserVO); // Mock 用此寫法

        UserVO targetVO = userController.getUserById("9576655d-6c9a-4eae-8b54-e6e7a5895fe6");
        System.out.println("targetVO = " + targetVO);
    }

}
