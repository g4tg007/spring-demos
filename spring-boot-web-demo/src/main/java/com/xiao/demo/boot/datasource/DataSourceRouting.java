package com.xiao.demo.boot.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by xiao on 2018/7/28.
 */
public class DataSourceRouting extends AbstractRoutingDataSource {
    private final static Logger log = LoggerFactory.getLogger(DataSourceRouting.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DataSourceContextHandler.getDataSource();
        log.debug("datasource:current datasource is {}", dataSource);
        return dataSource;
    }

}
