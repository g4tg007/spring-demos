package com.xiao.demo.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiao on 2018/6/22.
 */
@Configuration
@EnableAutoConfiguration
@EnableEurekaClient
@RestController
public class ApplicationStarter {
    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }


    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

}
