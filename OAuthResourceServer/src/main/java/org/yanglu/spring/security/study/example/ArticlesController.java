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
    @GetMapping("/test3")
    public String test3() {
        log.info("333333333... ...");
        return "我是资源服务器test333333333";
    }
}
