package com.xiao.demo.context.service.impl;

import com.xiao.demo.context.dao.UserDao;
import com.xiao.demo.context.mapper.UserInfoMapper;
import com.xiao.demo.context.model.UserInfo;
import com.xiao.demo.context.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserById(Integer id){
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        return userInfo;
    }


    @Override
    public String login(String user) {

        return userDao.getUser(user);
    }
}
