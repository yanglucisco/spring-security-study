package org.yanglu.spring.security.study.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class MyController {
    @GetMapping("test")
    public String test(HttpServletRequest request) {
        var session = request.getSession();
        return "我是client1的资源, sessionID: " + session.getId();
    }
}
