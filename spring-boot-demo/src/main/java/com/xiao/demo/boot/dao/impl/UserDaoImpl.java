package com.xiao.demo.boot.dao.impl;


import com.xiao.demo.boot.dao.UserDao;
import com.xiao.demo.boot.datasource.DataSourceKey;
import com.xiao.demo.boot.mapper.UserMapper;
import com.xiao.demo.boot.pojo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
