package org.yanglu.spring.security.study.example;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public RestLoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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

    public record LoginRequest(String username, String password) {
    }
}
