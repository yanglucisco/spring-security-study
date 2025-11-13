package org.yanglu.spring.security.study.example.config.security;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // @Autowired
    // private UserService userService; // 你的业务服务

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据username查询你的用户实体
        // PasswordEncoder encoder =
        // PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // UserDetails user = User.builder()
        // .username("admin")
        // .password("admin")
        // .passwordEncoder(encoder::encode)
        // .roles("USER")
        // .authorities("1;2;3")
        // .build();
        // if (user == null) {
        // throw new UsernameNotFoundException("用户未找到: " + username);
        // }
        // return user;

        // // 2. 查询该用户的权限/角色列表
        // List<String> permissions =
        // permissionService.findPermissionsByUserId(user.getId());

        // // 3. 构建并返回自定义的CustomUserDetails
        if ("admin".equals(username)) {
            return new CustomUserDetails(
                    1L,
                    "admin",
                    "{noop}adminpwd",
                    "",
                    List.of("ROLE_admin", "ROLE_normal"),
                    true, true, true, true // 账户状态
            );
        }
        return new CustomUserDetails(
                1L,
                "yanglu",
                "{noop}yanglupwd",
                "",
                List.of("ROLE_normal"),
                true, true, true, true // 账户状态
        );
    }
}
