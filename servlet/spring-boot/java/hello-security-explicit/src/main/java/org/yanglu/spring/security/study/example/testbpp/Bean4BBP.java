package org.yanglu.spring.security.study.example.testbpp;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
@Data
public class Bean4BBP implements InitializingBean {
    private String test = "123";
    public Bean4BBP(){
        log.info("测试 construct Bean4BBP");
    }
    public void test(){
        log.info("测试 Bean4BBP test test");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("测试 Bean4BBP afterPropertiesSet " + test);
    }
}
