package com.xiao.demo.boot.utils;

import org.junit.Test;

import java.sql.Types;

import static org.junit.Assert.*;

/**
 * Created by xiao on 2018/8/16.
 */
public class SqlTypeTest {
    @Test
    public void valueOf() throws Exception {
        SqlType type = SqlType.valueOf("DATETIME");

        Class clazz = type.clazz;

        int type2 = MysqlDefs.mysqlToJavaType("DATETIME");
        assertNotNull(type);
    }

    @Test
    public void getSetConversions() throws Exception {

    }

    @Test
    public void canSet() throws Exception {

    }

    @Test
    public void canGet() throws Exception {

    }

}