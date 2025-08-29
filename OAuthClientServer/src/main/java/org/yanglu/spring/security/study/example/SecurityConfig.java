package org.yanglu.spring.security.study.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/articles/**").hasAuthority("SCOPE_articles.read")
//                .requestMatchers("/test/**").hasAuthority("SCOPE_server")
//                .anyRequest().authenticated()
//        )
        http
                .authorizeHttpRequests(authorizeRequests ->authorizeRequests
                                .requestMatchers("/test2/**").permitAll()
                        .requestMatchers("/test_post/**").permitAll()

                                .requestMatchers("/web/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(
//                        Customizer.withDefaults()
                        oauth2Login ->oauth2Login.loginPage("/oauth2/authorization/articles-client-oidc")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Client(withDefaults());
        return http.build();
    }
}
