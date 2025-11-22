package org.yanglu.spring.security.study.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import static org.springframework.security.config.Customizer.withDefaults;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
        @Autowired
        private RedirectToOriginalUrlAuthenticationSuccessHandler successHandler;
        // @Autowired
        // private ReactiveClientRegistrationRepository clientRegistrationRepository;

        @Bean
        WebClient webClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
                // 创建 OAuth2 过滤器功能函数，这是自动携带令牌的关键
                ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                                authorizedClientManager);

                // 可选：设置为自动使用已授权的客户端（例如，基于当前安全上下文）
                oauth2Filter.setDefaultOAuth2AuthorizedClient(true);
                // 可选：设置默认的客户端注册ID，如果请求时未指定则使用此ID
                // oauth2Filter.setDefaultClientRegistrationId("your-client-registration-id");
                // 构建 WebClient 并添加 OAuth2 过滤器
                return WebClient.builder()
                                .filter(oauth2Filter) // 添加OAuth2令牌中继功能
                                // .clientConnector(new ReactorClientHttpConnector(httpClient))
                                .build();
        }

        @Bean
        public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
                        ReactiveClientRegistrationRepository clientRegistrationRepository,
                        ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {

                ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder
                                .builder()
                                .clientCredentials()
                                .build();

                DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager = new DefaultReactiveOAuth2AuthorizedClientManager(
                                clientRegistrationRepository, authorizedClientRepository);
                authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

                return authorizedClientManager;
        }

        @Bean
        SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
                        ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) throws Exception {
                // 实例化自定义解析器
                CustomAuthorizationRequestResolver customResolver = new CustomAuthorizationRequestResolver(
                                reactiveClientRegistrationRepository);
                http.authorizeExchange(exchange -> exchange
                                .pathMatchers("/", "/*.css", "/*.js", "/*.html", "/favicon.ico",
                                                "/test123123", "/login", "/resourcerole123/roleadmin",
                                                "/rsi4j/**", "/fallback/**", "/testresil4j/**" )
                                .permitAll()
                                .anyExchange().authenticated())
                                // 前后端分离项目，请求后端数据时，不应该返回302，而是应该返回401
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new HttpStatusServerEntryPoint(
                                                                HttpStatus.UNAUTHORIZED)))
                                .oauth2Login(
                                                // withDefaults()
                                                c -> c.authorizationRequestResolver(customResolver) // 注册自定义解析器
                                                                .authenticationSuccessHandler(successHandler)

                                )
                                .logout(logout -> logout.logoutSuccessHandler(
                                                // 定义一个自定义的处理器，用于退出操作成功完成的场景
                                                oidcLogoutSuccessHandler(reactiveClientRegistrationRepository)))
                                .csrf(csrf -> csrf.csrfTokenRepository(
                                                CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                                                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
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
