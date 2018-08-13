package com.shusheng.mybatis.auto.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.shusheng.mybatis.auto.generator.*")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
