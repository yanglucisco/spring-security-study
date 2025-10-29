package org.yanglu.spring.security.study.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OAuthClientServer1App {
    public static void main(String[] args) {
        System.out.println( "Hello OAuthClientServer1App!" );
        SpringApplication.run(OAuthClientServer1App.class, args);
    }
}