package org.yanglu.spring.security.study.example.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.yanglu.spring.security.model.User;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class TyemeleafController {
    @GetMapping("/test")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("name", "张三");
        model.addAttribute("date", LocalDate.now());
        var session = request.getSession();
        var isLogin = session.getAttribute("isLogin");
        if(null == isLogin){
            return "login";
        }
        return "transfermoney";
    }
    @GetMapping("/test1")
    public String test1() {
        return "test1"; 
    }
    @GetMapping("/login")
    public String login() {
        return "login"; 
    }
    @PostMapping("loginaction")
    public String loginaction(@ModelAttribute User user, HttpServletRequest request) {
        if("123@123.com".equals(user.getEmail()) && "password".equals(user.getPassword()))
        {
            var session = request.getSession();
            session.setAttribute("isLogin", "true");
            return "home";
        }
        return "login";
    }
    @GetMapping("/home")
    public String home(HttpServletRequest request) {
        var session = request.getSession();
        var isLogin = session.getAttribute("isLogin");
        if(null == isLogin){
            return "login";
        }
        return "home";
    }
}
