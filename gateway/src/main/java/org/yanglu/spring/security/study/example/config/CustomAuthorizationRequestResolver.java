package org.yanglu.spring.security.study.example.config;

import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomAuthorizationRequestResolver extends DefaultServerOAuth2AuthorizationRequestResolver {

    private final WebSessionServerRequestCache requestCache = new WebSessionServerRequestCache();
    private static final String PARAM_REDIRECT_URI = "redirect_uri";

    public CustomAuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        super(clientRegistrationRepository);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        // 从请求参数中获取redirect_uri
        String redirectUri = exchange.getRequest().getQueryParams().getFirst(PARAM_REDIRECT_URI);
        if (redirectUri != null && !redirectUri.isEmpty()) {
            // 保存到WebSession中
            return this.requestCache.saveRequest(exchange).then(Mono.defer(() -> {
                return super.resolve(exchange);
            }));
        }
        return super.resolve(exchange);
    }
}
