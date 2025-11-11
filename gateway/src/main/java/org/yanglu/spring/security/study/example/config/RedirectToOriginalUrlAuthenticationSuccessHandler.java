package org.yanglu.spring.security.study.example.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@Component
public class RedirectToOriginalUrlAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private WebSessionServerRequestCache requestCache = new WebSessionServerRequestCache();

    /**
     * 使用 Spring 的 UriComponentsBuilder 解析查询参数
     */
    public static void parseWithSpring(URI uri) {
        MultiValueMap<String, String> queryParams = UriComponentsBuilder
                .fromUri(uri)
                .build()
                .getQueryParams();

        System.out.println("查询参数: " + queryParams);

        // 获取特定参数（第一个值）
        String redirectUri = queryParams.getFirst("redirect_uri");
        String name = queryParams.getFirst("name");

        System.out.println("重定向URI: " + redirectUri);
        System.out.println("姓名: " + name);
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        URI uri = null;
        try {
            uri = new URI("/");
        } catch (URISyntaxException ex) {
        }
        // 从请求缓存中获取原始URL
        return requestCache.getRedirectUri(exchange)
                .defaultIfEmpty(uri) // 如果找不到缓存URL，则使用默认首页（如"/"）
                .flatMap(redirectUrl -> {
                    try {
                        MultiValueMap<String, String> queryParams = UriComponentsBuilder
                                .fromUri(redirectUrl)
                                .build()
                                .getQueryParams();
                        String redirectUri = queryParams.getFirst("redirect_uri");
                        // String query = redirectUrl.getQuery();
                        // String redirectUri =
                        // exchange.getRequest().getQueryParams().getFirst("redirect_uri");
                        final URI uri1 = new URI(redirectUri);
                        // 执行重定向
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FOUND);
                        exchange.getResponse().getHeaders().setLocation(uri1);
                        return exchange.getResponse().setComplete();
                    } catch (URISyntaxException ex) {
                        return exchange.getResponse().setComplete();
                    }
                });
    }
}
