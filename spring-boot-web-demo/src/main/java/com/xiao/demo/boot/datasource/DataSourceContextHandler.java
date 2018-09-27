package com.xiao.demo.boot.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiao on 2018/7/28.
 */
public class DataSourceContextHandler {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static List<String> dataSourceIds = new ArrayList<String>();

    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static String getDataSource() {
        return (String) contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
    public static boolean containsDataSource(String dataSourceId){

        return dataSourceIds.contains(dataSourceId);

    }
}
