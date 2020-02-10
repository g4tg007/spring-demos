package com.xiao.demo.context.service;

import com.xiao.demo.context.model.UserInfo;

public interface UserService {
    UserInfo getUserById(Integer id);

    String login(String user);
}
