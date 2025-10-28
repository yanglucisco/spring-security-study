package org.yanglu.spring.security.study.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ArticlesController {

    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/articles")
    public String[] getArticles(
            @RegisteredOAuth2AuthorizedClient("articles-client-authorization-code") OAuth2AuthorizedClient authorizedClient
    ) {
        return this.webClient
                .get()
                .uri("http://127.0.0.1:8090/articles")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }
    @GetMapping(value = "/articles1")
    public String getArticles1(HttpServletRequest request 
        //     @RegisteredOAuth2AuthorizedClient("articles-client-authorization-code") OAuth2AuthorizedClient authorizedClient
    ) {
        var session = request.getSession();
        return "我是client的资源, sessionID: " + session.getId();
    }
    @GetMapping(value = "/test")
    public String test(
            @RegisteredOAuth2AuthorizedClient("articles-client-authorization-server") OAuth2AuthorizedClient authorizedClient
    ){
//        return "test";
        return this.webClient
                .get()
                .uri("http://auth-resource:8090/test")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @GetMapping(value = "/test1")
    public String test1(
            @RegisteredOAuth2AuthorizedClient("articles-client-authorization-server") OAuth2AuthorizedClient authorizedClient
    ){
//        return "test1";
        return this.webClient
                .get()
                .uri("http://auth-resource:8090/test1")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @GetMapping(value = "/test2")
    public String test2(){
        return "test1222222222222222222222222222222222";
    }
    @GetMapping(value = "/test3")
    public String test3(
            @RegisteredOAuth2AuthorizedClient("articles-browser") OAuth2AuthorizedClient authorizedClient
    ){
//        return "test1";
        return this.webClient
                .get()
                .uri("http://auth-resource:8090/test3")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    @GetMapping(value = "/test_password")
    public String testPassword(
    ){
        return "testPassword-testPassword-testPassword-testPassword";
    }
    @PostMapping(value = "/test_post")
    public YlTest testPost(){
        YlTest ylTest = new YlTest();
        ylTest.setAge(12);
        ylTest.setName("yanglu");
        return ylTest;
    }
}
