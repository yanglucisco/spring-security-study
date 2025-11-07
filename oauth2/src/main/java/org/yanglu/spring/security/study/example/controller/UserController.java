package org.yanglu.spring.security.study.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("user")
    public String getUser1(Authentication authentication) {
        return "";
    }
}
