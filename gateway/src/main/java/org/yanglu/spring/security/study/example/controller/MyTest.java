package org.yanglu.spring.security.study.example.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class MyTest {
    @GetMapping("test")
    public String test() {
        return "test";
    }
    @PostMapping("login")
    public String login() {
        return "post-login";
    }
    @GetMapping("login")
    public String login1() {
        return "get-login";
    }
}
