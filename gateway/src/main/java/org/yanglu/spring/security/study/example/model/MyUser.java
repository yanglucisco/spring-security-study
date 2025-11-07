package org.yanglu.spring.security.study.example.model;

import java.util.List;

public record MyUser(
        String username,
        String authroies,
        List<String> roles) {

}
