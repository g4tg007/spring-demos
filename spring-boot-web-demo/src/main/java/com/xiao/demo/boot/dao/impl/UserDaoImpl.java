package com.xiao.demo.boot.dao.impl;


import com.xiao.demo.boot.dao.UserDao;
import com.xiao.demo.boot.datasource.DataSourceKey;
import com.xiao.demo.boot.mapper.UserMapper;
import com.xiao.demo.boot.pojo.model.User;
import com.xiao.demo.boot.utils.SqlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(Integer id) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        String sql = "select * from user ";

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement pre = con.prepareStatement(sql);
            ResultSet res = pre.executeQuery();

            ResultSetMetaData rsmd = res.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int i = 1;i<=columnCount;i++){
                String name = rsmd.getColumnName(i);
                String type = rsmd.getColumnTypeName(i);
                String clazz = rsmd.getColumnClassName(i);
                logger.debug(name + "》" +type);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Object object = jdbcTemplate.queryForList(sql);

        return userMapper.selectByPrimaryKey(id);
    }

}
