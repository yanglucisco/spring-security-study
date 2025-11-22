package org.yanglu.spring.security.study.example.resourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.yanglu.spring.security.study.example.common.CustomJwtAuthenticationConverter;

@Configuration
@EnableWebSecurity(debug = true)
public class ResourceServerConfig {
        private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;
        public ResourceServerConfig(CustomJwtAuthenticationConverter customJwtAuthenticationConverter) {
        this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
    }
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                    // Customizer.withDefaults()
                    jwt->jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)
                    ))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/testcf/**").permitAll()
                                .requestMatchers("/testResi4j/**").permitAll()
                                .requestMatchers("/articles/**").hasAuthority("SCOPE_articles.read")
                                .requestMatchers("/test/**").hasAuthority("SCOPE_server")
                                .requestMatchers("/test1/**").hasAuthority("SCOPE_server")
                                .requestMatchers("/test3/**").hasAuthority("SCOPE_browser")
                                .requestMatchers("/pkce/**").hasAuthority("SCOPE_pkce")
                                .requestMatchers("/resourcerole0/roleadmin").hasRole("admin")
                                .requestMatchers("/resourcerole0/rolenormal").hasRole("normal")
                                .requestMatchers("/resourcescope/**").hasAuthority("SCOPE_articles.read")
                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
                return http.build();
        }
}
