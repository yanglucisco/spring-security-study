package org.yanglu.spring.security.study.example.config;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

//@Component
public class MyAuthorizationManager implements AuthorizationManager<Authentication> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, Authentication object) {
        return null;
    }
}
