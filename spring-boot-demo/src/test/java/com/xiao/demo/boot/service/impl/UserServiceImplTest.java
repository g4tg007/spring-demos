package com.xiao.demo.boot.service.impl;

import com.xiao.demo.BaseTest;
import com.xiao.demo.boot.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceImplTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void login() {
        String user = userService.login("test");
        assertNotNull(user);
    }
}