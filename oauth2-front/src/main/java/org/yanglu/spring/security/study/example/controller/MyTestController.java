package org.yanglu.spring.security.study.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MyTestController {
    @GetMapping("fronttest")
    public String test() {
        return "test";
    }
}
