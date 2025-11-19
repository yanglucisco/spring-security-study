package org.yanglu.spring.security.study.example.resourceserver.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.yanglu.spring.security.study.example.resourceserver.dto.Book;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RestController
@RequestMapping("resourcescope0")
@Slf4j
public class TestScopeController {
    private final WebClient webClient;
    public TestScopeController(WebClient webClient){
        this.webClient = webClient;
    }
    @GetMapping("test")
    public Book test(Authentication authentication, HttpServletRequest request,
    @RegisteredOAuth2AuthorizedClient("resource-server") OAuth2AuthorizedClient authorizedClient) {
        @SuppressWarnings("null")
        var r = webClient
            .get()
            .uri("/resourcescope/book")
            .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
            .retrieve()
            .bodyToMono(Book.class)
            .defaultIfEmpty(new Book(1, "未找到改图书", ""))
            .timeout(Duration.ofSeconds(2), Mono.just(new Book(1, "超时未找到图书", "" )))
            .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
            .onErrorResume(Exception.class, ex ->  Mono.just(new Book(1, "重试3次仍未找到图书", "" )))
            .block();
        return r;
    }
}
