package com.xiao.demo;

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@Configuration
@ComponentScan(basePackages = {"com.xiao.demo.boot.service","com.xiao.demo.boot.dao"})
@Import({

})
public class BaseTest extends TestCase {

}
