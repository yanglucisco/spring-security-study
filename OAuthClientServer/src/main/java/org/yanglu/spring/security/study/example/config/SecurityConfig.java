package org.yanglu.spring.security.study.example.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.yanglu.spring.security.study.example.filters.CustomFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
        @Autowired
        private CustomFilter customFilter; // 注入自定义的过滤器

        @Bean
        @SuppressWarnings("unused")
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                .requestMatchers("/test2/**").permitAll()
                                                .requestMatchers("/test_post/**").permitAll()

                                                .requestMatchers("/web/**").permitAll()
                                                // .requestMatchers("/articles1/**").permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(
                                                oauth2Login -> oauth2Login.loginPage(
                                                                "/oauth2/authorization/articles-client-oidc"))
                                // .csrf(AbstractHttpConfigurer::disable)
                                .oauth2Client(withDefaults());
                return http.build();
        }

        // @Bean
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
