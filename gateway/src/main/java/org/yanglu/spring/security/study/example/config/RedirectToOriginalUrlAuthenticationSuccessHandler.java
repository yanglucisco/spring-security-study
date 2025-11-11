package org.yanglu.spring.security.study.example.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class RedirectToOriginalUrlAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private WebSessionServerRequestCache requestCache = new WebSessionServerRequestCache();

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        URI uri = null;
        try {
            uri = new URI("/index1.html");
        } catch (URISyntaxException ex) {
        }
        // 从请求缓存中获取原始URL
        return requestCache.getRedirectUri(exchange)
                .defaultIfEmpty(uri) // 如果找不到缓存URL，则使用默认首页（如"/"）
                .flatMap(redirectUrl -> {
                    try
                    {
                    final URI uri1 = new URI("/index1.html");
                    // 执行重定向
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FOUND);
                    exchange.getResponse().getHeaders().setLocation(redirectUrl);
                    return exchange.getResponse().setComplete();
                    }catch(URISyntaxException ex)
                    {
                        return exchange.getResponse().setComplete();
                    }
                });
    }
}
