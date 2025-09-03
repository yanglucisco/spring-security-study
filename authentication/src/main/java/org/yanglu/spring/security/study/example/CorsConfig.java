package org.yanglu.spring.security.study.example;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    //    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 允许所有路径
//                .allowedOrigins("http://127.0.0.1:8082") // 允许所有来源
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
//                .allowedHeaders("*") // 允许的请求头
//                .allowCredentials(true) // 是否允许携带 Cookie
//                .maxAge(3600); // 预检请求的缓存时间（秒）                               // 预检请求缓存时间
//    }


}
