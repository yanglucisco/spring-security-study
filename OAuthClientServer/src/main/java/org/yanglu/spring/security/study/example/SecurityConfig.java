package org.yanglu.spring.security.study.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@EnableWebSecurity(debug = true)
public class SecurityConfig {

//    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/articles/**").hasAuthority("SCOPE_articles.read")
//                .requestMatchers("/test/**").hasAuthority("SCOPE_server")
//                .anyRequest().authenticated()
//        )
        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeRequests ->authorizeRequests
                                .requestMatchers("/test2/**").permitAll()
                        .requestMatchers("/test_post/**").permitAll()

                                .requestMatchers("/web/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(
//                        Customizer.withDefaults()
                        oauth2Login ->oauth2Login.loginPage("/oauth2/authorization/articles-client-oidc")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Client(withDefaults());
        return http.build();
    }
//    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://auth-server:9000")); // 生产环境避免使用通配符*[5](@ref)[6](@ref)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 必须包含OPTIONS[2](@ref)[7](@ref)
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        config.setAllowCredentials(true); // 允许凭证需配合具体域名[2](@ref)[5](@ref)
        config.setMaxAge(3600L); // 预检请求缓存时间[5](@ref)[7](@ref)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 应用到所有路径[1](@ref)[4](@ref)
        return source;
    }
}
