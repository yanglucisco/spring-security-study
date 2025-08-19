package org.yanglu.spring.security.study.example.pages.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.yanglu.spring.security.study.example.CustomAuthenticationProvider;

/**
 * @author YangLu
 * @version 1.0
 * @description desc
 * @date 2025/8/19 10:50
 **/
@Configuration
@EnableWebSecurity
@Slf4j
public class RestfullSecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("调用filter");
        http
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/test", "/index.html",
                                "/restlogin/test1", "/restlogin/test").permitAll().anyRequest().authenticated()
                )
                .formLogin(form -> form.loginPage("/login").permitAll());
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        CustomAuthenticationProvider authenticationProvider =
                new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
//        authenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(authenticationProvider);
    }
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
