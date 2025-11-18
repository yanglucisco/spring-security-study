package org.yanglu.spring.security.study.example.resilience4jtest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Resi4jTest {

  public static void main(String[] args) {
    test1();
  }

  private static void test2() {
    // Create a CircuitBreaker with default configuration
    CircuitBreaker circuitBreaker = CircuitBreaker
        .ofDefaults("backendService");

    // Create a Retry with default configuration
    // 3 retry attempts and a fixed time interval between retries of 500ms
    Retry retry = Retry
        .ofDefaults("backendService");

    // Create a Bulkhead with default configuration
    Bulkhead bulkhead = Bulkhead
        .ofDefaults("backendService");
    var backendService = new Resi4jTestService();
    Supplier<String> supplier = () -> backendService.test1();

    // Decorate your call to backendService.doSomething()
    // with a Bulkhead, CircuitBreaker and Retry
    // **note: you will need the resilience4j-all dependency for this
    Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
        .withCircuitBreaker(circuitBreaker)
        .withBulkhead(bulkhead)
        .withRetry(retry)
        .decorate();

    // When you don't want to decorate your lambda expression,
    // but just execute it and protect the call by a CircuitBreaker.
    String result = circuitBreaker
        .executeSupplier(backendService::test1);

    // You can also run the supplier asynchronously in a ThreadPoolBulkhead
    ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead
        .ofDefaults("backendService");

    // The Scheduler is needed to schedule a timeout
    // on a non-blocking CompletableFuture
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));

    CompletableFuture<String> future = Decorators.ofSupplier(supplier)
        .withThreadPoolBulkhead(threadPoolBulkhead)
        .withTimeLimiter(timeLimiter, scheduledExecutorService)
        .withCircuitBreaker(circuitBreaker)
        .withFallback(List.of(TimeoutException.class,
            CallNotPermittedException.class,
            BulkheadFullException.class),
            throwable -> "Hello from Recovery")
        .get().toCompletableFuture();
  }

  private static void test1() {
    // Create a custom configuration for a CircuitBreaker
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(50)// 失败率
        .waitDurationInOpenState(Duration.ofMillis(10000))// 断路器打开后多长时间进入半打开状态
        .permittedNumberOfCallsInHalfOpenState(2)// 进入半打开状态后，允许调用再次执行的次数
        .slidingWindowSize(2)// 调用最小次数
        // .recordExceptions(IOException.class, TimeoutException.class, RuntimeException.class)
        // .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
        .build();

    // Create a CircuitBreakerRegistry with a custom global configuration
    CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
    CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("name");
    // var backendService = new Resi4jTestService();
    Supplier<String> supplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
      int i = ThreadLocalRandom.current().nextInt(20, 100);
        if(i > 61){
            throw new RuntimeException("值大于61了，所以抛出了异常");
        }
        // log.info("测试 Resi4jTestService.test");
        return "正常";
    });
    Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
        .withCircuitBreaker(circuitBreaker)
        // .withBulkhead(bulkhead)
        // .withRetry(retry)
        .withFallback(List.of(
          RuntimeException.class,
            IOException.class,
            TimeoutException.class,
            CallNotPermittedException.class,
            BulkheadFullException.class),
            throwable -> "请稍后再试")
        .decorate();
    // String result = Try.ofSupplier(decoratedSupplier).recover(throwable -> "Hello from Recovery").get();
    for(int i = 1;i<100;i++){
      var str = decoratedSupplier.get();
      log.info(i + " 结果:" + str);
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
