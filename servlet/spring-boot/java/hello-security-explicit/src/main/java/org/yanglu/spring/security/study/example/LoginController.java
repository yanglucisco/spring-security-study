package org.yanglu.spring.security.study.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class LoginController {
    @GetMapping("/login")
    String login() {
        return "login";
    }
//    @PostMapping("/logout")
//    String logout() {
//        return "login";
//    }
}
