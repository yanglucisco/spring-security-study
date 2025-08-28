package org.yanglu.spring.security.study.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangLu
 * @version 1.0
 * @description desc
 * @date 2025/8/28 15:57
 **/
@RestController
public class MyTestController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
