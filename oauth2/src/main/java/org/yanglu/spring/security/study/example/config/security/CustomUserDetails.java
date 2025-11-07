package org.yanglu.spring.security.study.example.config.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

// 示例：一个包含用户ID和手机号等额外信息的自定义UserDetails
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 3795011598734000565L;

    private Long id; // 自定义用户ID
    private String username;
    private String password;
    private String phone; // 自定义字段：手机号
    private List<String> permissions; // 自定义字段：权限列表
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    // 构造函数、Getter和Setter ...
    
    // 实现UserDetails接口规定的方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将你的permissions转换为GrantedAuthority集合
        return this.permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public String getUsername() { return this.username; }

    @Override
    public boolean isAccountNonExpired() { return this.accountNonExpired; }

    @Override
    public boolean isAccountNonLocked() { return this.accountNonLocked; }

    @Override
    public boolean isCredentialsNonExpired() { return this.credentialsNonExpired; }

    @Override
    public boolean isEnabled() { return this.enabled; }
}

