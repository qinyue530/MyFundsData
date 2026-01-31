package com.example.myfunds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@MapperScan("com.example.myfunds.mapper")
public class MyFundsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFundsApplication.class, args);
    }

}