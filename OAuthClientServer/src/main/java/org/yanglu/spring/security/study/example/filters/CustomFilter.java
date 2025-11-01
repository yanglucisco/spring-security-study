package org.yanglu.spring.security.study.example.filters;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.yanglu.spring.security.study.example.config.MyTest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component
// @PreFilter(value = "")
public class CustomFilter extends OncePerRequestFilter {

    public CustomFilter(org.yanglu.spring.security.study.example.config.MyTest mytest) {
        this.mytest = mytest;
    }
    private final MyTest mytest;
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        // 在这里添加自定义逻辑
//        chain.doFilter(request, response);
//    }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        mytest.test();
        filterChain.doFilter(request, response);
   }
}
