package org.yanglu.spring.security.study.example.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
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
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.yanglu.spring.security.study.example.config.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity(debug = true)
public class DefaultSecurityConfig {
    @SuppressWarnings("unused")
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    @Order(1)
    @SuppressWarnings("unused")
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults()) // Enable OpenID Connect 1.0
        ;
        http
                // 配置异常处理，当需要认证时重定向到我们的自定义登录页
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/custom-login")))
                .oauth2ResourceServer(oauth2 -> 
                oauth2.jwt(Customizer.withDefaults()
                    // jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) // 应用自定义转换器
                )
                );

        return http.formLogin(withDefaults()).build();
    }

    @Bean
    @Order(2)
    @SuppressWarnings("unused")
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/themeleaftest/**").permitAll()
                        .requestMatchers("/login.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/custom-login", "/login").permitAll()
                        .requestMatchers("/home").permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(
                        formLogin -> formLogin
                                // 指定自定义登录页的URL
                                .loginPage("/custom-login")
                                // 指定处理登录认证的POST请求地址，与HTML表单action一致
                                .loginProcessingUrl("/login")
                // 登录成功后的默认跳转页面
                // .defaultSuccessUrl("/home", true)
                );
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置 JWT 令牌中权限声明（claims）的名称，默认为 "scope" 或 "scopes"
        // 如果你的角色信息放在另一个claim里，修改这个值，例如 "roles"
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // 设置权限前缀，如果不需要前缀可以设置为空字符串，这里设置为空以便直接使用自定义角色名
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    // @Bean
    // @SuppressWarnings("unused")
    // UserDetailsService users() {
    // PasswordEncoder encoder =
    // PasswordEncoderFactories.createDelegatingPasswordEncoder();
    // UserDetails user = User.builder()
    // .username("admin")
    // .password("password")
    // .passwordEncoder(encoder::encode)
    // .roles("USER")
    // .authorities("1;2;3")
    // .build();
    // return new InMemoryUserDetailsManager(user);
    // }

    @Bean
    @SuppressWarnings("unused")
    RegisteredClientRepository registeredClientRepository() {
        RegisteredClient articlesClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("articles-client")
                .clientSecret("{noop}secret")
                .clientName("Articles Client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                    // gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .redirectUris((uris -> {
                    uris.add("http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc");
                    uris.add("http://127.0.0.1:8080/authorized");
                }))
                .scopes(s -> {
                    s.add("openid");
                    s.add("articles.read");
                    // s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofMinutes(3)).build())
                .build();
        // RegisteredClient articlesClientRead = RegisteredClient.withId(UUID.randomUUID().toString())
        //         .clientId("articles-client-read")
        //         .clientSecret("{noop}readsecret")
        //         .clientName("Articles Client Read")
        //         .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        //         .authorizationGrantTypes(gts -> {
        //             gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
        //             gts.add(AuthorizationGrantType.REFRESH_TOKEN);
        //             gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
        //         })
        //         .redirectUris((uris -> {
        //             uris.add("http://127.0.0.1:8080/login/oauth2/code/articles-client-read");
        //             uris.add("http://127.0.0.1:8080/authorized");
        //         }))
        //         .postLogoutRedirectUri("http://127.0.0.1:10000")
        //         .scopes(s -> {
        //             s.add("openid");
        //             s.add("articles.read");
        //         })
        //         .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
        //         .build();

        RegisteredClient articlesClient1 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("articles-client1")
                .clientSecret("{noop}secret1")
                .clientName("Articles Client1")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                    // gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .redirectUris((uris -> {
                    uris.add("http://127.0.0.1:8081/login/oauth2/code/articles-client1-oidc");
                    uris.add("http://127.0.0.1:8081/authorized");
                }))
                .scopes(s -> {
                    s.add("openid");
                    s.add("articles.read");
                    s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofMinutes(3)).build())
                .build();

        RegisteredClient gateway = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("gateway")
                .clientSecret("{noop}gatewaysecret")
                .clientName("gateway")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
                    gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .redirectUris((uris -> {
                    uris.add("http://127.0.0.1:10000/login/oauth2/code/gateway");
                    uris.add("http://127.0.0.1:10000/authorized");
                }))
                .postLogoutRedirectUri("http://127.0.0.1:10000")
                .scopes(s -> {
                    s.add("openid");
                    s.add("articles.read");
                    // s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                // .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofMinutes(3)).build())
                .build();

        List<RegisteredClient> clients = new ArrayList<>();
        clients.add(articlesClient);
        clients.add(articlesClient1);
        clients.add(gateway);
        // clients.add(articlesClientRead);

        return new InMemoryRegisteredClientRepository(clients);
    }

}
