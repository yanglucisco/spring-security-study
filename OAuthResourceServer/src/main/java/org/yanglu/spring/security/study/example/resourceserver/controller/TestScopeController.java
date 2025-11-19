package org.yanglu.spring.security.study.example.resourceserver.controller;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("resourcescope0")
@Slf4j
public class TestScopeController {
    private final WebClient webClient;
    public TestScopeController(WebClient webClient){
        this.webClient = webClient;
    }
    @GetMapping("test")
    public String test(Authentication authentication, HttpServletRequest request,
    @RegisteredOAuth2AuthorizedClient("resource-server") OAuth2AuthorizedClient authorizedClient) {
        @SuppressWarnings("null")
        var r = webClient
            .get()
            .uri("/resourcescope/test")
            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return "我是resource0000000的资源testscope，当前时间:" + LocalDateTime.now().toString() + "/r/n" +
        r;
    }
}
