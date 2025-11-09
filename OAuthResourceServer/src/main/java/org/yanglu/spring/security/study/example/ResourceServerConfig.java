package org.yanglu.spring.security.study.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //    http.securityMatcher("/articles/**")
    //            .authorizeHttpRequests(authorize ->
    //                    authorize.anyRequest()
    //                    .hasAuthority("SCOPE_aritcles.read"))
    //            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/articles/**").hasAuthority("SCOPE_articles.read")
                .requestMatchers("/test/**").hasAuthority("SCOPE_server")
                        .requestMatchers("/test1/**").hasAuthority("SCOPE_server")
                        .requestMatchers("/test3/**").hasAuthority("SCOPE_browser")
                        .requestMatchers("/pkce/**").hasAuthority("SCOPE_pkce")
                .anyRequest().authenticated()
        )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
        ;
        return http.build();
    }
}
