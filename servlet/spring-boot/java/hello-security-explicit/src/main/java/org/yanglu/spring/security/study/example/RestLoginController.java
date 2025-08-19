package org.yanglu.spring.security.study.example;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author YangLu
 * @version 1.0
 * @description desc
 * @date 2025/8/19 10:38
 **/
@RestController
@RequestMapping("/restlogin")
public class RestLoginController {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    public RestLoginController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("test")
    public String test(){
        return "test";
    }
    @GetMapping("test1")
    public String login(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(userName, password);
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        return "成功";
    }
    @GetMapping("update-password")
    public String updatePassword(@RequestParam(value = "password") String password){
        String newStr = passwordEncoder.encode(password);
        return newStr;
    }

    public record LoginRequest(String username, String password) {
    }
}
