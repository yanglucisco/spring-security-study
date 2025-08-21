package org.yanglu.spring.security.study.example.config;

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
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
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
//        .defaultSuccessUrl("/index") // 登录成功后跳转到指定页面
//                .failureUrl("/login-error.html")
        http
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/index.html","/favicon.ico","/index","/error","/test",
                                "/hello.html",
                                "/restlogin/test1", "/restlogin/test").permitAll()
                                .requestMatchers("/testaut").hasRole("USER")
                                .anyRequest().authenticated()
                )
//                .passwordManagement((management) -> management
//                        .changePasswordPage("/update-password")
//                )
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/index.html")
                        .failureUrl("/error").permitAll());
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
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username("user")
//                .password("$argon2id$v=19$m=16384,t=2,p=1$1dmntPi6T5vXRMgKHV5baQ$NLFA8dJBgupBrrSdD37xo1SyRS4bqdtEY5Nc+tRsGJ4")
////                .password("{bcrypt}$2a$10$FPIefipSMFV8xdpBDFH72uGEicyUEGvKcAoTou6pGZze.dJlulumO")
//                //         {bcrypt}$2a$10$jdPMWXpanVAE5gWVqUgoKe0KAC6oTf0twQerQ6Ca4Z0sRzeXN3.0O
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder pe = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
//        String s = pe.encode("password");
//         PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        BCryptPasswordEncoder r = new BCryptPasswordEncoder();
        String s1 = pe.encode("password");
//        String s2 = SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8().encode("password");
        String s3 = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("password");
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return pe;
    }

}
