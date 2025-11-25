package org.yanglu.spring.security.study.example.resourceserver.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testrabbitmq")
public class TestRabbitMQController {
    private final RabbitTemplate rabbitTemplate;
    public TestRabbitMQController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }
    @GetMapping("test")
    public String test(){
        // 例如，发送一个Map对象
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "这是一个消息");
        map.put("data", Arrays.asList("hello", 123, LocalDateTime.now().toString()));
        
        // 直接发送对象，RabbitTemplate会自动将其转换为JSON
        rabbitTemplate.convertAndSend("certificate.event.topic1", "a.first", map);
        // rabbitTemplate.convertAndSend("certificate.event.topic1", "a.first", "Hello from RabbitMQ!");
        return "testrabbitmq/test : " + LocalDateTime.now().toString();
    }
}
