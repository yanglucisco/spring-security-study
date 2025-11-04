package org.yanglu.spring.security.study.example.config;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String handleException(Exception e) {
        System.out.println("全局异常捕获>>>:" + e);
        return "全局异常捕获, 错误原因>>>" + e.getMessage();
    }

    @ExceptionHandler(value = OAuth2AuthenticationException.class)
    @ResponseBody
    public String handleBizException(OAuth2AuthenticationException e) {
        System.out.println("发生业务异常！原因是：" + e.getMessage());
        return "OAuth2AuthenticationException异常捕获, 错误原因>>>" + e.getMessage();
    }
}
