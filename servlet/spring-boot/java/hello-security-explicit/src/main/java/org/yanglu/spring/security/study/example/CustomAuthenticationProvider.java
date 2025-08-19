package org.yanglu.spring.security.study.example;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 获取用户输入的用户名和密码
        String username = authentication.getName();

        String password = authentication.getCredentials().toString();

        // 2. 从数据库加载用户详情
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 3. 验证密码
        String p1 = passwordEncoder.encode(password);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        // 4. 返回认证成功的令牌（可附加额外信息）
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 声明支持 UsernamePasswordAuthenticationToken 类型的认证
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

