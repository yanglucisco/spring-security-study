package org.yanglu.spring.security.study.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

@Configuration
@EnableWebSecurity(debug = true)
public class DefaultSecurityConfig {
    @Bean
    @Order(1)
    @SuppressWarnings("unused")
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults()) // Enable OpenID Connect 1.0

        ;

        return http.formLogin(withDefaults()).build();
    }
    @Bean
    @Order(2)
    @SuppressWarnings("unused")
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // .requestMatchers("/oauth2/authorize").permitAll()
                        // .requestMatchers("/test").permitAll()
                        .anyRequest()
                        .authenticated())
                // .csrf(AbstractHttpConfigurer::disable)
                .formLogin(withDefaults())
        ;
        return http.build();
    }
    @Bean
    @SuppressWarnings("unused")
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
//                    gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .redirectUris((uris -> {
                    uris.add("http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc");
                    uris.add("http://127.0.0.1:8080/authorized");
                }))
                .scopes(s -> {
                    s.add("openid");
                    s.add("articles.read");
                    s.add("server");
                })
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();
        
        RegisteredClient articlesClient1 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("articles-client1")
                .clientSecret("{noop}secret1")
                .clientName("Articles Client1")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(gts -> {
                    gts.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    gts.add(AuthorizationGrantType.REFRESH_TOKEN);
//                    gts.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
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
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build()).build();

        List<RegisteredClient> clients = new ArrayList<>();
        clients.add(articlesClient);
        clients.add(articlesClient1);

        return new InMemoryRegisteredClientRepository(clients);
    }

}
