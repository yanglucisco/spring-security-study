package org.yanglu.spring.security.study.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("themeleaftest")
public class MyThemeLeafTestController {
    @GetMapping("test1")
    public String test1(){
        return "test1";
    }  
}
