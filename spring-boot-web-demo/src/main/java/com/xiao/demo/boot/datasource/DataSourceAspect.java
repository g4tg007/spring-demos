package com.xiao.demo.boot.datasource;

import java.util.regex.Pattern;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by xiao on 2018/7/28.
 */
@Aspect
@Order(-10)//保证该AOP在@Transactional之前执行
@Component
public class DataSourceAspect {
    private final static Logger log = LoggerFactory.getLogger(DataSourceAspect.class);

    /*

     * @Before("@annotation(ds)")

     * 的意思是：

     * @Before：在方法执行之前进行执行：

     * @annotation(targetDataSource)：

     * 会拦截注解targetDataSource的方法，否则不拦截;

     */
    @Before("@annotation(dataSourceKey)")
    public void changeDataSource(JoinPoint point, DataSourceKey dataSourceKey) throws Throwable {

        //获取当前的指定的数据源;
        String dsId = dataSourceKey.value();
        //如果不在我们注入的所有的数据源范围之内，那么输出警告信息，系统自动使用默认的数据源。
        if (!DataSourceContextHandler.containsDataSource(dsId)) {
            log.error("数据源[{}]不存在，使用默认数据源 > {}"+ dataSourceKey.value()+point.getSignature());
        } else {
            log.debug("Use DataSource : {} > {}"+dataSourceKey.value()+point.getSignature());
            //找到的话，那么设置到动态数据源上下文中。
            DataSourceContextHandler.setDataSource(dataSourceKey.value());
        }
    }



    @After("@annotation(dataSourceKey)")
    public void restoreDataSource(JoinPoint point, DataSourceKey dataSourceKey) {
        log.debug("Revert DataSource : {} > {}"+dataSourceKey.value()+point.getSignature());
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        DataSourceContextHandler.clearDataSource();
    }

}
