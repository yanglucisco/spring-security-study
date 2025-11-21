package org.yanglu.spring.security.study.example.gateway.resi4j.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.netty.handler.timeout.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class Resi4jTestService {
    private static final String BACKEND_A = "backendA";
    private final WebClient webClient;
    // private final ReactiveCircuitBreaker circuitBreaker;
    // private final io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;

    public Resi4jTestService(WebClient webClient
        // , CircuitBreakerRegistry registry
    // ,ReactiveCircuitBreakerFactory circuitBreakerFactory
    ) {
        this.webClient = webClient;
        // this.circuitBreaker = circuitBreakerFactory.create("backendA");
        // this.circuitBreaker = registry.circuitBreaker(BACKEND_A);
    }

    @CircuitBreaker(name = BACKEND_A, fallbackMethod = "fallback")
    
    public String testLocalCall() {
        System.out.println("方法A exec service a " + LocalDateTime.now().toString());
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
        // var backendCall = this.webClient
        // .get()
        // .uri("http://127.0.0.1:8090/articles")
        // .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
        // .retrieve()
        // .bodyToMono(String.class);
        // return backendCall;
        // 2. 使用熔断器运行主逻辑，并指定降级策略
        // return circuitBreaker.run(
        // backendCall,
        // throwable -> {
        // // 3. Fallback 逻辑：当熔断器开启或调用失败时执行
        // log.error("调用外部服务失败，触发降级", throwable);
        // return Mono.just("{\"message\": \"服务暂不可用，请稍后重试123123\"}");
        // }
        // );
    }

    @SuppressWarnings("null")
    @CircuitBreaker(name = BACKEND_A, fallbackMethod = "monoFallback")
    @TimeLimiter(name = BACKEND_A) 
    public Mono<String> testRemoteCall(OAuth2AuthorizedClient authorizedClient) {
        var backendCall =
        this.webClient
        .get()
        .uri("http://127.0.0.1:9081/backendA/test")
        // .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
        .retrieve()
        .bodyToMono(String.class);
        // 使用操作符装饰，并定义降级逻辑
        return backendCall;
    }
    // 降级方法
    private String fallback(HttpServerErrorException ex) {
        // 这里可以记录日志、返回一个默认值、缓存数据等
        // String[] strs = new String[1];
        // strs[0] = "服务暂时不可用，请稍后重试。 (Fallback响应)";
        return LocalDateTime.now() + "服务暂时不可用，请稍后重试。 (Fallback响应)";
    }

    private String fallback(Exception ex) {
        // 这里可以记录日志、返回一个默认值、缓存数据等
        // String[] strs = new String[1];
        // strs[0] = "服务暂时不可用，请稍后重试。 (Fallback响应)";
        return LocalDateTime.now() + "断路器打开了";
    }
    private Mono<String> monoFallback(Exception ex) {
        return Mono.just("monoFallback Recovered: " + LocalDateTime.now().toString() + ": " + ex.toString());
    }
    private Mono<String> monoFallback(java.util.concurrent.TimeoutException ex){
        return Mono.just("TimeoutException monoFallback Recovered: " + LocalDateTime.now().toString() + ": " + ex.toString());
    }
}
