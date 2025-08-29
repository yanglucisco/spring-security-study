package org.yanglu.spring.security.study.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ArticlesController {

    @GetMapping("/articles")
    public String[] getArticles() {
        log.info("获取articles... ...");
        return new String[] { "Article 1", "Article 2", "Article 3" };
    }
    @GetMapping("/test")
    public String test() {
        log.info("test... ...");
        return "获取test成功";
    }
    @GetMapping("/test1")
    public String test1() {
        log.info("test1111111... ...");
        return "我是资源服务器test11111111";
    }
    @GetMapping("/test2")
    public String test2() {
        log.info("22222222... ...");
        return "我是资源服务器test222222222";
    }
    @GetMapping("/test3")
    public String test3() {
        log.info("3333333333... ...");
        return "我是资源服务器test3333333333333";
    }
    @GetMapping("/test_password")
    public String testPassword() {
        log.info("testPassword... ...");
        return "我是资源服务器testPassword-testPassword-testPassword";
    }
}
