package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oauth2FrontApp {
    public static void main(String[] args) {
        System.out.println("Hello Oauth2FrontApp!");
        SpringApplication.run(Oauth2FrontApp.class, args);
    }
}