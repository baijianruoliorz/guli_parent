package com.atguigu.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/*
 * author:liqiqiorz
 * data:2020.05.27
 * */
@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu"})
@EnableDiscoveryClient
@EnableFeignClients //服务调用，代码很固定。。
public class EduApplication {
    //ComponentScan这个注解是：config类一般在项目编译时会有扫描，但是不在同一个moudle下不会，为了防止这种情况发生，要进行一次扫描。
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
