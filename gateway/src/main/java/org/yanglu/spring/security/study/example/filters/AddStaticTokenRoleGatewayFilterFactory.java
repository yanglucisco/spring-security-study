package org.yanglu.spring.security.study.example.filters;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
// @Slf4j
public class AddStaticTokenRoleGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Autowired
    private ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            Mono<Authentication> authenticationMono = 
            ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication);
            return authenticationMono.flatMap(authentication -> {
                if (authentication != null && authentication.isAuthenticated()) {

                    String tv = ((DefaultOidcUser) ((OAuth2AuthenticationToken) authentication).getPrincipal()).getIdToken().getTokenValue();

//                    OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
//                                .withClientRegistrationId("gateway")
//                                .principal(authentication)
//                                .attribute(ServerWebExchange.class.getName(), exchange)
//                                .build();
//                    this.authorizedClientManager.authorize(authorizeRequest).subscribe(a -> {
//                        System.out.println("测试");
//                    });

                    Mono<ServerHttpRequest> req = Mono.just(tv)
                                .map( token -> {
                                    ServerHttpRequest request = exchange.getRequest().mutate()
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                    .build();
                                    return request;
                                });
                    return req.flatMap(r -> {
                        return chain.filter(exchange.mutate().request(r).build());
                    });
                } else {
                    // 未认证处理：返回401错误
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            });
        };
    }
}
