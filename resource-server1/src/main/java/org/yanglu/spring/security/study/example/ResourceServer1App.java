package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServer1App {
    public static void main(String[] args) {
        System.out.println("Hello ResourceServer1App!");
        SpringApplication.run(ResourceServer1App.class, args);
    }
}