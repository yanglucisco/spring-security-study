package org.yanglu.spring.security.study.example.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("testloggroup")
@Slf4j
public class TestLogGroupController {
    @GetMapping("test")
    public String test(){
        log.trace("这是追踪级别的日志信息");
        return "testloggroup/test : " + LocalDateTime.now();
    }
}
