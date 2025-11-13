package org.yanglu.spring.security.study.example.config.security;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

// 示例：一个包含用户ID和手机号等额外信息的自定义UserDetails
// 添加以下注解
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) // 在JSON中包含类信息
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略未知属性
@JsonSerialize // 声明该类可序列化
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 3795011598734000565L;

    private Long id; // 自定义用户ID
    private String username;
    private String password;
    private String phone; // 自定义字段：手机号
//    @JsonIgnore
    private List<String> permissions; // 自定义字段：权限列表
//    @JsonIgnore
    private boolean accountNonExpired;
//    @JsonIgnore
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    // 构造函数、Getter和Setter ...
    
    // 实现UserDetails接口规定的方法
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将你的permissions转换为GrantedAuthority集合
        List<GrantedAuthority> r = this.permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        List<GrantedAuthority> r1 = new ArrayList<>(r);
        return r1;
    }

//    @Override
//    public String getPassword() { return this.password; }
//
//    @Override
//    public String getUsername() { return this.username; }

    @Override
//    @JsonIgnore
    public boolean isAccountNonExpired() { return this.accountNonExpired; }

    @Override
//    @JsonIgnore
    public boolean isAccountNonLocked() { return this.accountNonLocked; }

    @Override
//    @JsonIgnore
    public boolean isCredentialsNonExpired() { return this.credentialsNonExpired; }

    @Override
//    @JsonIgnore
    public boolean isEnabled() { return this.enabled; }
}

