package org.yanglu.spring.security.study.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
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

    @GetMapping("/testaut")
    public String testaut(){
        return "testaut";
    }

    @GetMapping("/testaut1")
    public String testaut1(){
        return "testaut1";
    }

}

