package org.yanglu.spring.security.study.example.config;

import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


final class SpaCsrfTokenRequestHandler implements ServerCsrfTokenRequestHandler {

    @Override
    public void handle(ServerWebExchange exchange,
            Mono<org.springframework.security.web.server.csrf.CsrfToken> csrfToken) {
                csrfToken.subscribe(s -> {
                    System.out.println("token = " + s);
                    this.resolveCsrfTokenValue(exchange, s);
                });
        
    }
	
}
