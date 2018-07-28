package com.xiao.demo.boot.dao;

import com.xiao.demo.boot.pojo.model.User;

public interface UserDao {
    User getById(Integer id);
}
