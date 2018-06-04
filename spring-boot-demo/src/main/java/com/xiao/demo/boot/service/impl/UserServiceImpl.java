package com.xiao.demo.boot.service.impl;

import com.xiao.demo.boot.dao.UserDao;
import com.xiao.demo.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public String login(String user) {

        return userDao.getUser(user);
    }
}