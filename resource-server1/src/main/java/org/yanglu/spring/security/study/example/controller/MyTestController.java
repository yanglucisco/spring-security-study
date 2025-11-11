package org.yanglu.spring.security.study.example.controller;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("resource1")
public class MyTestController {
    @GetMapping("test")
    public String getMethodName() {
        return "我是resource1的资源，当前时间:" + LocalDateTime.now().toString();
    }
    
}
