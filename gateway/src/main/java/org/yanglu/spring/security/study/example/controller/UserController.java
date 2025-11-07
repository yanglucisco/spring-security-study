package org.yanglu.spring.security.study.example.controller;

import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Mono;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yanglu.spring.security.study.example.model.MyUser;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {
    @GetMapping("user")
    public Mono<MyUser> getUser() {
        return ReactiveSecurityContextHolder.getContext() // ←---
                                                          // 从ReactiveSecurityContextHolder中获取当前认证用户的SecurityContext
                .map(SecurityContext::getAuthentication) // ←--- 从SecurityContext中获取Authentication
                .map(authentication -> {
                    var authors = authentication.getAuthorities();
                    List<String> authorList = new ArrayList<>();
                    for (GrantedAuthority authoritie : authors) {
                        authorList.add(authoritie.getAuthority());
                    }
                    MyUser user = new MyUser(authentication.getName(), String.join(";", authorList), List.of(""));
                    return user;
                });
    }
    @GetMapping("user1")
    public Mono<MyUser> getUser1(Authentication authentication) {
        return Mono.empty();
    }
}
