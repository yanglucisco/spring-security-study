package org.yanglu.spring.security.study.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Bean
    @SuppressWarnings("unused")
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // .requestMatchers("/test2/**").permitAll()
                        // .requestMatchers("/test_post/**").permitAll()

                        // .requestMatchers("/web/**").permitAll()
                        // .requestMatchers("/test/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(
                        oauth2Login -> oauth2Login.loginPage(
                                "/oauth2/authorization/articles-client1-oidc"))
                // .csrf(AbstractHttpConfigurer::disable)
                .oauth2Client(withDefaults());
        return http.build();
    }
}
