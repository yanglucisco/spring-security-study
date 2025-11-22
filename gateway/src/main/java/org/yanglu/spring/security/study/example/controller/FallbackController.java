package org.yanglu.spring.security.study.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fallback")
public class FallbackController {
    @GetMapping("backenda")
    public String backendA()
    {
        return "这是backendA的fallback url";
    }
}
