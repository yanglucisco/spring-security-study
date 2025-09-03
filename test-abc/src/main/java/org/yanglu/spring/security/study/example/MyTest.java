package org.yanglu.spring.security.study.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangLu
 * @version 1.0
 * @description desc
 * @date 2025/9/3 13:05
 **/
@RestController
public class MyTest {
    @GetMapping("/test")
    public MyTestJson test(){
        MyTestJson r = new MyTestJson();
        r.setName("ceshi");
        return r;
    }
}
