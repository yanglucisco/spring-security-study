package org.yanglu.spring.security.study.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/custom-login") // 这个路径将在安全配置中指定为登录页
    public String loginPage() {
        return "login"; // 返回模板名称（对应login.html）
    }
}
