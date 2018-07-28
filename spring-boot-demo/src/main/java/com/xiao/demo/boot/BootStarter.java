package com.xiao.demo.boot;

import com.xiao.demo.boot.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableAutoConfiguration
//@ComponentScan(basePackages = {"com.xiao.demo.boot.service","com.xiao.demo.boot.dao"})
//@SpringBootApplication
public class BootStarter {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BootStarter.class, args);

        System.out.println("context 启动成功");

        UserService service = context.getBean(UserService.class);
        System.out.println(service.getById(1));

    }
}
