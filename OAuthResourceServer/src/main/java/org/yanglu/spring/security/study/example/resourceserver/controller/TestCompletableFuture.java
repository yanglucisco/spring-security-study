package org.yanglu.spring.security.study.example.resourceserver.controller;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testcf")
public class TestCompletableFuture {
    @GetMapping("test")
    public CompletableFuture<String> test(){
        CompletableFuture<String> future = new CompletableFuture<>();
         CompletableFuture.supplyAsync(() -> {
            return LocalDateTime.now();
        }).whenComplete((a, b) -> {
            future.complete(a.toString());
        });
        return future;
    }
}
