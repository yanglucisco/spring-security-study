package org.yanglu.spring.security.study.example.config;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
class AppConfig {

    AppConfig() {
        System.out.println("AppConfig init...");
    }

//    @Bean
    BeanPostProcessor postProcessor() {
        return new MyBeanPostProcessor();
    }
}



