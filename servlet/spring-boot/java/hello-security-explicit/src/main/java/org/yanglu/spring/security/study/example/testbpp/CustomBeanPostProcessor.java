package org.yanglu.spring.security.study.example.testbpp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author YangLu
 * @version 1.0
 * @description desc
 * @date 2025/8/21 12:18
 **/
//@Component
@Slf4j
public class CustomBeanPostProcessor implements BeanPostProcessor {
    public CustomBeanPostProcessor() {
        log.info("测试 construct CustomBeanPostProcessor");
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Bean4BBP) {
            log.info("测试 process bean before initialization");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Bean4BBP) {
            log.info("测试 process bean after initialization");
        }
        return bean;
    }
}
