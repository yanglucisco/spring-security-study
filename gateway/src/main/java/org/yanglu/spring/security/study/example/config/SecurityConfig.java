package org.yanglu.spring.security.study.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
        @Bean
        @SuppressWarnings("unused")
        SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
                        ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) throws Exception {
                http.authorizeExchange(exchange -> exchange
                                .pathMatchers("/", "/*.css", "/*.js", "/*.html", "/favicon.ico",
                                                "/test", "/login")
                                .permitAll()
                                .anyExchange().authenticated())
                                // 前后端分离项目，请求后端数据时，不应该返回302，而是应该返回401
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new HttpStatusServerEntryPoint(
                                                                HttpStatus.UNAUTHORIZED)))
                                .oauth2Login(Customizer.withDefaults())
                                .logout(logout -> logout.logoutSuccessHandler(
                                                // 定义一个自定义的处理器，用于退出操作成功完成的场景
                                                oidcLogoutSuccessHandler(reactiveClientRegistrationRepository)))

                                .csrf(ServerHttpSecurity.CsrfSpec::disable) // 不应该禁用，后面要放开
                                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                                .oauth2Client(withDefaults());
                return http.build();
        }

        private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(
                        ReactiveClientRegistrationRepository clientRegistrationRepository) {
                var oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(
                                clientRegistrationRepository);
                oidcLogoutSuccessHandler
                                .setPostLogoutRedirectUri("{baseUrl}");
                // 从OIDC提供者退出之后，将会重定向至应用的基础URL，该URL是由Spring动态计算得到的（本地的话，将会是http://localhost:9000/）
                return oidcLogoutSuccessHandler;
        }
}
