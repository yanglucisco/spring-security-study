package org.yanglu.spring.security.study.example.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class MyTest {
    // @Autowired
	// private ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    @Autowired
    private WebClient webClient;

	// public MyTest(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
	// 	this.webClient = webClient;
	// }
    @GetMapping("test")
    public String test() {
        return "test";
    }
    @GetMapping("test1")
    public String test1() {
        return "test";
    }
    @GetMapping("test2")
    public Mono<String[]> test2(
        // Authentication authentication, ServerWebExchange exchange
        @RegisteredOAuth2AuthorizedClient("gateway1") OAuth2AuthorizedClient authorizedClient
    ) {
        var r = authorizedClient.getAccessToken().getTokenValue();
         return this.webClient
                .get()
                .uri("http://127.0.0.1:8090/articles")
                .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String[].class);
    }
    @PostMapping("login")
    public String login() {
        return "post-login";
    }
    @GetMapping("login")
    public String login1() {
        return "get-login";
    }
}
