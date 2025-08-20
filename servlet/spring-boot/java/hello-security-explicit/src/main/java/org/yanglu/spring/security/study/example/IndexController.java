package org.yanglu.spring.security.study.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    }@GetMapping("/error")
    public String error() {
        return "error";
    }

}

