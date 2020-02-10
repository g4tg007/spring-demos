package com.xiao.demo.context.service.impl;

import com.xiao.demo.BaseTest;
import com.xiao.demo.context.model.UserInfo;
import com.xiao.demo.context.service.UserService;
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

    @Test
    public void select() throws Exception {
        UserInfo userInfo = userService.getUserById(1);
        assertNotNull(userInfo);
    }
}