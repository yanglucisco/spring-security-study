package org.yanglu.spring.security.study.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class YangLuConfig {
    @Bean
    public YangLuFilter1 bean1(){
        log.info("YangLuFilterYangLuFilterYangLuFilterYangLuFilter");
        return new YangLuFilter1();
    }
}
