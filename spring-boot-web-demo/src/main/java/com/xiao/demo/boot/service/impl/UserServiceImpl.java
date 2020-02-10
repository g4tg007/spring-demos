package com.xiao.demo.boot.service.impl;

import com.xiao.demo.boot.dao.UserDao;
import com.xiao.demo.boot.datasource.DataSourceKey;
import com.xiao.demo.boot.mapper.SummaryMtdpPageMonitorMapper;
import com.xiao.demo.boot.pojo.model.User;
import com.xiao.demo.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private SummaryMtdpPageMonitorMapper summaryMtdpPageMonitorMapper;

    @Override
    public User getById(Integer id) {
        return userDao.getById(id);
    }

    @Override
    @DataSourceKey(value = "local")
    public User getAnnotherById(Integer id) {
        return userDao.getById(id);
    }

    @Override
//    @DataSourceKey(value = "kylin")
    public List findSummaryList() {
        return summaryMtdpPageMonitorMapper.findList();
    }
}
