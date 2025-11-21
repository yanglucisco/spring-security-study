package org.yanglu.spring.security.study.example.resourceserver.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("testResi4j")
@Slf4j
public class TestResi4jController {
    @GetMapping("test")
    public String test(){
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now().toString() + " This is a remote exception");
        // try {
        //     Thread.sleep(200000);
        // } catch (InterruptedException e) {
        // }
        // log.info("exec testResi4j.test() exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()exec testResi4j.test()");
        // return "testResi4j.test() " + LocalDateTime.now().toString();
    }
}
