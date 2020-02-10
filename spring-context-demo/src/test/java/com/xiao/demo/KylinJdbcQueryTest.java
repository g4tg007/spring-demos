package com.xiao.demo;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class KylinJdbcQueryTest extends BaseTest{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void query() throws SQLException  {
        String sql = "SELECT\n" +
                "SUMMARY_PAGE_CUBE.PAGE_IDENTIFIER\n" +
                ",SUMMARY_PAGE_CUBE.PAGE_ID\n" +
                ",SUMMARY_PAGE_CUBE.APP_NAME\n" +
                ",SUMMARY_PAGE_CUBE.APP_VERSION\n" +
                ",SUMMARY_PAGE_CUBE.PLATFORM\n" +
                ",SUMMARY_PAGE_CUBE.GEO_CITY_ID\n" +
                ",SUMMARY_PAGE_CUBE.LOG_CHANNEL\n" +
                ",SUMMARY_PAGE_CUBE.PARTITION_DATE\n" +
                ",SUMMARY_PAGE_CUBE.PV\n" +
                ",SUMMARY_PAGE_CUBE.VALID_PV\n" +
                ",SUMMARY_PAGE_CUBE.DURATION\n" +
                ",SUMMARY_PAGE_CUBE.PAGE_NAME\n" +
                ",SUMMARY_PAGE_CUBE.GEO_CITY_NAME\n" +
                ",SUMMARY_PAGE_CUBE.DEVICE_ID\n" +
                "FROM MART_FLOW.SUMMARY_PAGE_CUBE as SUMMARY_PAGE_CUBE \n" +
                "where page_id = 40000787\n" +
                "and PARTITION_DATE = '2019-03-28'\n" +
                "limit 1000";


        jdbcTemplate.query(sql, new RowMapper<Object>() {
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                resultSet.getObject(1);
                return null;
            }
        });

    }
}