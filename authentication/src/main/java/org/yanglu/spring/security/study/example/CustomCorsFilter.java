package org.yanglu.spring.security.study.example;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomCorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8082");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        //application/x-www-form-urlencoded Content-Type
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");
        //Access-Control-Allow-Private-Network: true
        res.addHeader("Access-Control-Allow-Private-Network", "true");
        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            response.getWriter().println("ok");
            return;
        }
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
