package com.xiao.demo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void query2() throws Exception {
        Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();
        String sql = "SELECT" +
                " SUMMARY_PAGE_CUBE.PAGE_IDENTIFIER" +
                ",SUMMARY_PAGE_CUBE.PAGE_ID" +
                ",SUMMARY_PAGE_CUBE.APP_NAME" +
                ",SUMMARY_PAGE_CUBE.APP_VERSION" +
                ",SUMMARY_PAGE_CUBE.PLATFORM" +
                ",SUMMARY_PAGE_CUBE.GEO_CITY_ID" +
                ",SUMMARY_PAGE_CUBE.LOG_CHANNEL" +
                ",SUMMARY_PAGE_CUBE.PARTITION_DATE" +
                ",SUMMARY_PAGE_CUBE.PV" +
                ",SUMMARY_PAGE_CUBE.VALID_PV" +
                ",SUMMARY_PAGE_CUBE.DURATION" +
                ",SUMMARY_PAGE_CUBE.PAGE_NAME" +
                ",SUMMARY_PAGE_CUBE.GEO_CITY_NAME" +
                ",SUMMARY_PAGE_CUBE.DEVICE_ID" +
                " FROM MART_FLOW.SUMMARY_PAGE_CUBE as SUMMARY_PAGE_CUBE " +
                " where page_id = 40000787" +
                " and PARTITION_DATE = '2019-03-28'" +
                " limit 1000";

        Properties info = new Properties();
        info.put("user", "data_flow_r");
        info.put("password", "GXkHL0JhU45vMaC?");
        Connection conn = driver.connect("jdbc:kylin://ghquery.data.vip.sankuai.com:80/data_flow", info);
        Statement state = conn.createStatement();
        ResultSet resultSet = state.executeQuery(sql);

        while (resultSet.next()) {
            assertEquals("PAGE_IDENTIFIER", resultSet.getString(1));
        }
    }
}