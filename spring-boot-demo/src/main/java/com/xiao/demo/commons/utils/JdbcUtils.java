package com.xiao.demo.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

/**
 * Created by xiaoliang
 * 2017.11.08 15:43
 *  获取单个连接用
 * @Version 1.0
 */
public class JdbcUtils {
    public static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    private Properties properties;
    private String jdbcDriver;
    private String url;
    private String username;
    private String password;

    public JdbcUtils(Properties jdbc) {
        this.properties = jdbc;
        this.url = properties.getProperty("jdbc.url");
        this.username = properties.getProperty("jdbc.username");
        this.password = properties.getProperty("jdbc.password");
        this.jdbcDriver = properties.getProperty("jdbc.driverClass");
    }

    public JdbcUtils(String jdbcDriver, String url, String username, String password) {
        this.jdbcDriver = jdbcDriver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws Exception {
        Class.forName(jdbcDriver);
        return DriverManager.getConnection(url, username, password);
    }


    public void closeStatement(Statement pre){
        if(pre!=null){
            try {
                pre.close();
            } catch (SQLException e) {
                logger.error("closeStatement error : ",e);
            }
        }
    }

    public void closeResultSet(ResultSet res){
        if(res!=null){
            try {
                res.close();
            } catch (SQLException e) {
                logger.error("closeResultSet error : ",e);
            }
        }
    }

    public void closeConnection(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("closeConnection error : ",e);
            }
        }
    }

}
