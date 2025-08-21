package org.yanglu.spring.security.study.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for "/".
 *
 * @author Joe Grandja
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index")
    public String index1() {
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
//        return "redirect:/hello.html";
    }
    @PostMapping("/test1")
    public String test1() {
        return "test";
//        return "redirect:/hello.html";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

}

