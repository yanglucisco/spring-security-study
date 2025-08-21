package org.yanglu.spring.security.study.example.testbpp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author YangLu
 * @version 1.0
 * @description desc
 * @date 2025/8/21 12:37
 **/
@Slf4j
//@Component
public class UseBean4BBP {
    private final Bean4BBP bean4BBP;

    public UseBean4BBP(Bean4BBP bean4BBP) {
        this.bean4BBP = bean4BBP;
        bean4BBP.setTest("test");
        log.info("设置 属性完毕");
    }
    public void test(){
        bean4BBP.test();;
    }
}
