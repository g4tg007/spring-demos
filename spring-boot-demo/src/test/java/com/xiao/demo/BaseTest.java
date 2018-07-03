package com.xiao.demo;

import com.xiao.demo.boot.BootStarter;
import com.xiao.demo.boot.WebStarter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
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
