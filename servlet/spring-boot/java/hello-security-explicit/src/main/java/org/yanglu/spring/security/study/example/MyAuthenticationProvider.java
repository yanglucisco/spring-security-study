package org.yanglu.spring.security.study.example;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author YangLu
 * @version 1.0
 * @description desc
 * @date 2025/8/18 12:40
 **/
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        boolean shouldAuthenticateAgainstThirdPartySystem = true;
        if (shouldAuthenticateAgainstThirdPartySystem) {

            // 使用凭证并对第三方系统进行身份认证
            return new UsernamePasswordAuthenticationToken(
                    name, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
