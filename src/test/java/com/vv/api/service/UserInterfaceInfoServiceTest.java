package com.vv.api.service;

import org.junit.jupiter.api.Assertions;
// 自动生成的包不对，要改成这个
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author vv先森
 * @create 2024-09-12 15:31
 */
@SpringBootTest
public class UserInterfaceInfoServiceTest {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;
    @Test
    public void invokeCount() {
        boolean b = userInterfaceInfoService.invokeCount(88L, 1L);
        Assertions.assertTrue(b);
    }
}