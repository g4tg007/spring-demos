package com.xiao.demo.boot.service.impl;

import com.xiao.demo.boot.dao.UserDao;
import com.xiao.demo.boot.datasource.DataSourceKey;
import com.xiao.demo.boot.pojo.model.User;
import com.xiao.demo.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getById(Integer id) {
        return userDao.getById(id);
    }
    @Override
    @DataSourceKey(value = "local")
    public User getAnnotherById(Integer id) {
        return userDao.getById(id);
    }
}
