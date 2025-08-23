package org.yanglu.spring.security.study.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        Map<String, Object> map = new HashMap<>();
        map.put("provider", "spring");
        ClientRegistration  clientRegistration = ClientRegistration
                .withRegistrationId("articles-client-oidc")
                .providerConfigurationMetadata(map)
                .clientId("articles-client")
                .clientSecret("secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/{registrationId}")
                .scope("openid")
                .clientName("articles-client-oidc")
                .authorizationUri(" https://accounts.google.com/o/oauth2/auth ")
                .tokenUri(" https://oauth2.googleapis.com/token ")
//                .userInfoUri(" https://www.googleapis.com/oauth2/v3/userinfo ")
//                .userNameAttributeName("sub")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .build();

        ClientRegistration  clientRegistration1 =
                ClientRegistration
                        .withRegistrationId("articles-client-authorization-code")
                        .providerConfigurationMetadata(map)
                        .clientId("articles-client")
                        .clientSecret("secret")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri("http://127.0.0.1:8080/authorized")
                        .scope("articles.read")
                        .clientName("articles-client-authorization-code")
                .authorizationUri(" https://accounts.google.com/o/oauth2/auth ")
                .tokenUri(" https://oauth2.googleapis.com/token ")
//                .userInfoUri(" https://www.googleapis.com/oauth2/v3/userinfo ")
//                .userNameAttributeName("sub")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .build();

        List<ClientRegistration> clientRegistrationList = new ArrayList<>();
        clientRegistrationList.add(clientRegistration);
        clientRegistrationList.add(clientRegistration1);
        return new InMemoryClientRegistrationRepository(
                clientRegistrationList
        );
    }
}
