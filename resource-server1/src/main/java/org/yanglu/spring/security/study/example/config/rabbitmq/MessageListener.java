package org.yanglu.spring.security.study.example.config.rabbitmq;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    // 当"myQueue"队列中有新消息时，此方法会自动被调用
    // @RabbitListener(queues = Demo20251124Application.queueName) // "myQueue"是你要监听的队列名称
    // public void receiveMessage(String message) {
    //     System.out.println("Received message: " + message);
    //     // 在这里编写你的业务处理逻辑
    // }
    @RabbitListener(queues = "certi.event.income1") // "myQueue"是你要监听的队列名称
    public void receiveMessage(Map<String, Object> message) {
        System.out.println("Received Map message: " + message);
        System.out.println("具体数据: " + message.get("msg"));
    }
}
