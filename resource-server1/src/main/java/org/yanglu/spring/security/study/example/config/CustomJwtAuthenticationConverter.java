package org.yanglu.spring.security.study.example.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // 从JWT中提取权限信息
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        // 创建包含权限的JwtAuthenticationToken
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // 1. 尝试从常见的声明中提取角色信息
        
        
        // 2. 如果roles声明为空，尝试其他常见声明名称
        // if (roles == null || roles.isEmpty()) {
        //     roles = jwt.getClaimAsStringList("authorities");
        // }
        List<String> scopesAndRoles = jwt.getClaimAsStringList("scope");
        List<String> roles = jwt.getClaimAsStringList("roles");
        scopesAndRoles.addAll(roles);
        if (!scopesAndRoles.isEmpty()) {
            return scopesAndRoles.stream()
                    // 确保角色有ROLE_前缀（hasRole()方法要求）
                    .map(item -> item.startsWith("ROLE_") ? item : "SCOPE_" + item)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        
        // 3. 将角色字符串转换为GrantedAuthority对象
        
        // List<GrantedAuthority> r = Collections.emptyList();
        
        // if (roles != null && !roles.isEmpty()) {
        //     return roles.stream()
        //             // 确保角色有ROLE_前缀（hasRole()方法要求）
        //             .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
        //             .map(SimpleGrantedAuthority::new)
        //             .collect(Collectors.toList());
        // }
        return Collections.emptyList();
        // List<String> scopes = jwt.getClaimAsStringList("scope");
        // if(scopes != null && !scopes.isEmpty())
        // {
        //     for (String scope : scopes) {
        //         GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + scope);
        //         r.add(grantedAuthority);
        //     }
        //     // r.addAll(scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        // }
        // return r;
    }
}

