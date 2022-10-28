package com.he.matchingsystem;

import com.he.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingSystemApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.matchingPool.start(); //启动匹配线程
        SpringApplication.run(MatchingSystemApplication.class,args);
    }
}