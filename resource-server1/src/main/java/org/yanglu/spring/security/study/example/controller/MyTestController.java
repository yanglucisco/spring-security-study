package org.yanglu.spring.security.study.example.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("resource1")
public class MyTestController {
    @GetMapping("testscope")
    public String getMethodName() {
        return "我是resource1的资源testscope，当前时间:" + LocalDateTime.now().toString();
    }
    @GetMapping("testroleadmin")
    public String testroleadmin(Authentication authentication, HttpServletRequest request) {
        // String authHeader = request.getHeader("Authorization");
        return "我是resource1的资源，当前用户必须是admin，当前时间:" + LocalDateTime.now().toString();
    }
    @GetMapping("testrolenormal")
    public String testrolenormal(Authentication authentication, HttpServletRequest request) {
        // String authHeader = request.getHeader("Authorization");
        return "我是resource1的资源，当前用户必须是normal，当前时间:" + LocalDateTime.now().toString();
    }
}
