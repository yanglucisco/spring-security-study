package org.yanglu.spring.security.study.example.config;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Supplier;

//@Component
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    //implements AuthorizationManager<RequestAuthorizationContext>
//    @Override
//    public AuthorizationDecision check(Supplier<Authentication> authentication, Authentication object) {
//        return null;
//    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        // 获取当前用户的权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
        return new AuthorizationDecision(false);
    }
}
