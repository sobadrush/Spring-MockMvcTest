package com.rogerlo.demo.springmockmvctest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringMockMvcTestApplicationTests {

    @Test
    @DisplayName("Test_001")
    void test_001(TestInfo testInfo) {
        System.out.println(" === " + testInfo.getDisplayName() + " === ");
    }

}
