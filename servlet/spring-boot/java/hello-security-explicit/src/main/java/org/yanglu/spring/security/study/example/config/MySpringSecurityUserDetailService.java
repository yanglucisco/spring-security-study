package org.yanglu.spring.security.study.example.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MySpringSecurityUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> gr =new ArrayList<>();
        gr.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_USER123";
            }
        });
        return new User("user", "{bcrypt}$2a$10$6d5pBnvXTBMoNynuomlYHO69LoFTnNouLBCulqe/bpp5y/qmLLSXu", gr);
    }
}
