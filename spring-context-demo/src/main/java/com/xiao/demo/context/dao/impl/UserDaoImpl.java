package com.xiao.demo.context.dao.impl;


import com.xiao.demo.context.dao.UserDao;
import com.xiao.demo.context.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public String getUser(String user) {

        return user;
    }
}
