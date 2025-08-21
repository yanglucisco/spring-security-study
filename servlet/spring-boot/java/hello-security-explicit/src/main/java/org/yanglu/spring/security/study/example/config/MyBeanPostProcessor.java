package org.yanglu.spring.security.study.example.config;

import org.springframework.beans.factory.config.BeanPostProcessor;

class MyBeanPostProcessor
        implements BeanPostProcessor
{

    MyBeanPostProcessor() {
        System.out.println("MyBeanPostProcessor init...");
    }
}