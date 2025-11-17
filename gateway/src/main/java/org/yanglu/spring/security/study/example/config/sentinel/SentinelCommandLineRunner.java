package org.yanglu.spring.security.study.example.config.sentinel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker.State;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SentinelCommandLineRunner implements CommandLineRunner {
    private static final String KEY = "some_method";

    private static volatile boolean stop = false;
    private static int seconds = 120;

    private static AtomicInteger total = new AtomicInteger();
    private static AtomicInteger pass = new AtomicInteger();
    private static AtomicInteger block = new AtomicInteger();
    @Override
    public void run(String... args) throws Exception {
        initFlowRules();
        // test1();
    }
    private void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(1);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
    @SuppressWarnings("unused")
    private void test1(){
        initTestResourceDegradeRules();
        registerStateChangeObserver();
        startTick();

        int concurrency = 8;
        for (int i = 0; i < concurrency; i++) {
            Thread entryThread = new Thread(() -> {
                while (true) {
                    Entry entry = null;
                    try {
                        entry = SphU.entry(KEY);
                        pass.incrementAndGet();
                        // RT: [40ms, 60ms)
                        sleep(ThreadLocalRandom.current().nextInt(45, 160));
                    } catch (BlockException e) {
                        block.incrementAndGet();
                        sleep(ThreadLocalRandom.current().nextInt(5, 10));
                    } finally {
                        total.incrementAndGet();
                        if (entry != null) {
                            entry.exit();
                        }
                    }
                }
            });
            entryThread.setName("sentinel-simulate-traffic-task-" + i);
            entryThread.start();
        }
    }
    private static void sleep(int timeMs) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeMs);
        } catch (InterruptedException e) {
            // ignore
        }
    }
    private void initTestResourceDegradeRules(){
        List<DegradeRule> rules = new ArrayList<>();
        //1 请求的响应时间大于50 ms则统计为慢调用
        //2 当单位统计时长（statIntervalMs 20000）内请求数目大于设置的最小请求数目（100）
        //3 并且慢调用的比例大于阈值（0.6）
        //4 则接下来的熔断时长（10s）内请求会自动被熔断
        DegradeRule rule = new DegradeRule(KEY)
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                /**
     * Threshold count. The exact meaning depends on the field of grade.
     * <ul>
     *     <li>In average RT mode, it means the maximum response time(RT) in milliseconds.</li>
     *     <li>In exception ratio mode, it means exception ratio which between 0.0 and 1.0.</li>
     *     <li>In exception count mode, it means exception count</li>
     * <ul/>
     */
                // Max allowed response time milliseconds
                .setCount(50)
                // Retry timeout (in second)
                .setTimeWindow(10)
                // Circuit breaker opens when slow request ratio > 60%
                .setSlowRatioThreshold(0.6)
                .setMinRequestAmount(100)
                .setStatIntervalMs(20000);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
        System.out.println("Degrade rule loaded: " + rules);
    }
    private void registerStateChangeObserver() {
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) -> {
                    if (newState == State.OPEN) {
                        System.err.println(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue));
                    } else {
                        System.err.println(String.format("%s -> %s at %d", prevState.name(), newState.name(),
                                TimeUtil.currentTimeMillis()));
                    }
                });
    }
    private void startTick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-tick-task");
        timer.start();
    }
    static class TimerTask implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("Begin to run! Go go go!");
            System.out.println("See corresponding metrics.log for accurate statistic data");

            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;

            while (!stop) {
                sleep(1000);

                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.println(TimeUtil.currentTimeMillis() + ", total:" + oneSecondTotal
                        + ", pass:" + oneSecondPass + ", block:" + oneSecondBlock);

                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.println("time cost: " + cost + " ms");
            System.out.println("total: " + total.get() + ", pass:" + pass.get()
                    + ", block:" + block.get());
            System.exit(0);
        }
    }
}
