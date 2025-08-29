package org.yanglu.spring.security.study.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.function.Consumer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true)
public class DefaultSecurityConfig {
    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        OAuth2AuthorizationServerConfigurer a = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults()); // Enable OpenID Connect 1.0

        return http.formLogin(withDefaults()).build();
    }
    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/oauth2/token").permitAll()
                        .anyRequest()
                        .authenticated())
//                .requestMatchers("/test/**").hasAuthority("SCOPE_server")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(withDefaults())
        ;
        return http.build();
    }
    @Bean
    UserDetailsService users() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
                .username("admin")
                .password("password")
                .passwordEncoder(encoder::encode)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
    @Bean
    RegisteredClientRepository registeredClientRepository() {
        RegisteredClient articlesClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("articles-client")
                .clientSecret("{noop}secret")
                .clientName("Articles Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .redirectUris((uris -> {
                    uris.add("http://auth-client:8080/login/oauth2/code/articles-client-oidc");
                    uris.add("http://auth-client:8080/authorized");
                }))
                .scopes(s -> {
                    s.add("openid");
                    s.add("articles.read");
                    s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();

        RegisteredClient accountClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId("account-service")
                .clientSecret("{noop}account-service-secret")
                .clientName("Account Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .scopes(s -> {
                    s.add("openid");
                    s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();

        RegisteredClient warehouseClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("warehouse-service")
                .clientSecret("{noop}warehouse-service-secret")
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .scopes(s -> {
                    s.add("server");
                }).clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();

        RegisteredClient browClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("browser-client")
                .clientSecret("{noop}browser-client")
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .scopes(s -> {
                    s.add("browser");
                }).clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();

        RegisteredClient passwordClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("browser-client1")
                .clientSecret("{noop}browser-client1")
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.JWT_BEARER);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .scopes(s -> {
                    s.add("browser");
                }).clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();

        List<RegisteredClient> clients = new ArrayList<>();
        clients.add(articlesClient);
        clients.add(accountClient);
        clients.add(warehouseClient);
        clients.add(browClient);
        clients.add(passwordClient);

        return new InMemoryRegisteredClientRepository(clients);
    }

}
