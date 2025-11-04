package org.yanglu.spring.security.study.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    @SuppressWarnings("unused")
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/", "/*.css", "/*.js", "/*.html", "/favicon.ico").permitAll()
                        .anyExchange().authenticated())
                //前后端分离项目，请求后端数据时，不应该返回302，而是应该返回401
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(Customizer.withDefaults())
                // .csrf(AbstractHttpConfigurer::disable) //不应该禁用，后面要放开
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .oauth2Client(withDefaults());
        return http.build();
    }
}

