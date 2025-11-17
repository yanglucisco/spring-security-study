package org.yanglu.spring.security.study.example.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yanglu.spring.security.study.example.config.sentinel.GatewayConfiguration;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("sentinel")
public class SentinelTestController {
    @GetMapping("test")
    // @SentinelResource("HelloWorld")
    public String test() {
        // 1.5.0 版本开始可以直接利用 try-with-resources 特性
        try (Entry entry = SphU.entry("HelloWorld")) {
            // 被保护的逻辑
            System.out.println("hello world");
            return "sentinel test " + LocalDateTime.now();
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            System.out.println("blocked!");
            return "限流了";
        }
    }

    @GetMapping("testdegrade")
    public String testDegrade() {
        Entry entry = null;
        try {
            entry = SphU.entry(GatewayConfiguration.SentinelTestDegradeName);
            // RT: [40ms, 60ms)
            sleep(ThreadLocalRandom.current().nextInt(45, 160));
            return "成功 " + LocalDateTime.now();
        } catch (BlockException e) {
            // block.incrementAndGet();
            sleep(ThreadLocalRandom.current().nextInt(5, 10));
            return "请求被降级了 " + LocalDateTime.now();
        } finally {
            // total.incrementAndGet();
            if (entry != null) {
                entry.exit();
            }
        }
    }
    private void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
