package org.yanglu.spring.security.study.example.common;

import java.util.ArrayList;
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
    public AbstractAuthenticationToken convert(@SuppressWarnings("null") Jwt jwt) {
        // 从JWT中提取权限信息
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        // 创建包含权限的JwtAuthenticationToken
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> all = new ArrayList<>();
        List<String> scopes = jwt.getClaimAsStringList("scope");
        if(scopes != null){
            all.addAll(scopes);
        }
        List<String> roles = jwt.getClaimAsStringList("roles");
        if(roles != null)
        {
            all.addAll(roles);
        }
        if (!all.isEmpty()) {
            return all.stream()
                    // 确保角色有ROLE_前缀（hasRole()方法要求）
                    .map(item -> item.startsWith("ROLE_") ? item : "SCOPE_" + item)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}


