package com.xiao.demo.boot;

import com.xiao.demo.boot.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class WebStarter {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BootStarter.class, args);

        System.out.println("context 启动成功");

        UserService service = context.getBean(UserService.class);
        // 这句将输出: hello world
        System.out.println(service.login(RandomStringUtils.randomAlphabetic(100)));
    }
}
