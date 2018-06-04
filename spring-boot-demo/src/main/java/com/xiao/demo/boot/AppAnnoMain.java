package com.xiao.demo.boot;


import com.xiao.demo.boot.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppAnnoMain {
    public static void main(String[] args) {

        // 用我们的配置文件来启动一个 ApplicationContext
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-anno.xml");
        System.out.println("context 启动成功");

        UserService service = context.getBean(UserService.class);
        // 这句将输出: hello world
        System.out.println(service.login(RandomStringUtils.randomAlphabetic(100)));

    }
}
