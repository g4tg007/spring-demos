package com.xiao.demo.boot.dao.impl;


import com.xiao.demo.context.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Override
    public String getUser(String user) {

        return user;
    }
}
