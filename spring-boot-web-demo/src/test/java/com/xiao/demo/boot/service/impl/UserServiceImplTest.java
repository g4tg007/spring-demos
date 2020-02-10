package com.xiao.demo.boot.service.impl;

import com.xiao.demo.BaseTest;
import com.xiao.demo.boot.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.Assert.*;

public class UserServiceImplTest extends BaseTest {

    @Autowired
    private UserService userService;


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void login() {

        DataSource dataSource = jdbcTemplate.getDataSource();
        userService.getAnnotherById(1);

    }
}