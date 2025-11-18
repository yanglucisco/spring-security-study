package org.yanglu.spring.security.study.example.resourceserver.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("resourcescope0")
public class TestScopeController {
    @GetMapping("test")
    public String test(Authentication authentication, HttpServletRequest request) {
        return "我是resource0000000的资源testscope，当前时间:" + LocalDateTime.now().toString();
    }
}
