package org.yanglu.spring.security.study.example.resilience4jtest;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Resi4jTestService {
    public String test(){
        int i = ThreadLocalRandom.current().nextInt(20, 100);
        if(i > 21){
            throw new RuntimeException("值大于50了，所以抛出IOException");
        }
        // log.info("测试 Resi4jTestService.test");
        return "Resi4jTestService.test";
    }
    public String test1(){
        return "test1";
    }
}
