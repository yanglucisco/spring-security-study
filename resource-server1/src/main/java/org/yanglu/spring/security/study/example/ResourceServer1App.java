package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yanglu.spring.security.study.example.common.MyCommonTest;

@SpringBootApplication
public class ResourceServer1App {
    public static void main(String[] args) {
        MyCommonTest myCommonTest = new MyCommonTest();
        myCommonTest.test();
        System.out.println("Hello ResourceServer1App!");
        SpringApplication.run(ResourceServer1App.class, args);
    }
}