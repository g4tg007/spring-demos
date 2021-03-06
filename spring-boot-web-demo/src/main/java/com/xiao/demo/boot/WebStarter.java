package com.xiao.demo.boot;

import com.xiao.demo.boot.datasource.MultiDataSourceRegister;
import com.xiao.demo.boot.pojo.model.User;
import com.xiao.demo.boot.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.xiao.demo.boot.mapper")
@Import({MultiDataSourceRegister.class})
public class WebStarter {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebStarter.class, args);

        System.out.println("context 启动成功");

        UserService service = context.getBean(UserService.class);
//        User user = service.getById(1);
//        User user2 = service.getAnnotherById(1);

        List list = service.findSummaryList();

        System.out.println();
    }
}
