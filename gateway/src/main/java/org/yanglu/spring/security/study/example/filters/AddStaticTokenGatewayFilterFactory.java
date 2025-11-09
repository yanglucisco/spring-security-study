package org.yanglu.spring.security.study.example.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AddStaticTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Autowired
	private ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    // 这里可以硬编码，或通过@Value从配置中心读取
    private static final String SPECIFIED_ACCESS_TOKEN = "eyJraWQiOiI3MDRmZTFjOC0wNGY5LTQ4MTgtYWViNC0zMTFlZGM4ZTYwNTQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJnYXRld2F5IiwiYXVkIjoiZ2F0ZXdheSIsIm5iZiI6MTc2MjY5MTQwOCwic2NvcGUiOlsiYXJ0aWNsZXMucmVhZCJdLCJpc3MiOiJodHRwOi8vYXV0aC1zZXJ2ZXI6OTAwMCIsImV4cCI6MTc2MjY5MTcwOCwiaWF0IjoxNzYyNjkxNDA4LCJqdGkiOiJkYmEzYTIwNC1kODQ3LTRjOWQtYTc4OS00NmE2NTlmNjRiNDQifQ.jHowb-BZItnSdUgnuff4neshVhB6nshHtcX4ZUpuTnYHkoSV7jxfGoNT1UBTtTa1NQPZBlxTahScjzNVY_5koVXKq3PYXjuJvY5EqSb_jVf-G88fcZD_389WvNYjSwp4HjrcPmiQ2vouXkM2tihUb6WlCE-K9ZxP1PqjjqsJkvxilekTAPaSOxJ-vD3TzvBqe6ALE-w8aDpdvxOkDiLxm9b5SD7nk_ho8npRY0o0ZjVL6EBZXVhWF-PLfSdDerM51Xl2Zz3dUAriHrO4VsEJs6NOcnYIAetZnGa4iRiF30tpSTxfpQmXctNkAjtdmG7r1nes16PZEXcPgSKMapIQUQ";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
        //     OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("gateway1")
		// 		.principal(authentication)
		// 		.attribute(ServerWebExchange.class.getName(), exchange)
		// 		.build();
        // this.authorizedClientManager.authorize(authorizeRequest)
		// 		.map(OAuth2AuthorizedClient::getAccessToken).subscribe(s -> {
        //             var str = s.getTokenValue();
        //         });
            // 将指定的令牌添加到下游请求的Authorization头
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + SPECIFIED_ACCESS_TOKEN)
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }
}

