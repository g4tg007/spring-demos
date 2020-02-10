package com.xiao.demo.boot.service;

import com.xiao.demo.boot.pojo.model.User;

import java.util.List;

public interface UserService {
    User getById(Integer id);

    User getAnnotherById(Integer id);

    List findSummaryList();
}
