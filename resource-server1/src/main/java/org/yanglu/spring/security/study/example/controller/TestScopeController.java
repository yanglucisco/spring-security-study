package org.yanglu.spring.security.study.example.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yanglu.spring.security.study.example.resourceserver1.dto.Book;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("resourcescope")
public class TestScopeController {
    @GetMapping("test")
    public String test(Authentication authentication, HttpServletRequest request) {
        return "我是resource1的资源testscope，当前时间:" + LocalDateTime.now().toString();
    }
    @GetMapping("book")
    public Book book(Authentication authentication, HttpServletRequest request) {
        TestRoleController.sleep(5000);
        Book r = new Book(1, "book1", "isbn1");
        return r;
    }
}
