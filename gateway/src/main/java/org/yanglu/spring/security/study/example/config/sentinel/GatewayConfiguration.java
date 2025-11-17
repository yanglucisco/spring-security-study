package org.yanglu.spring.security.study.example.config.sentinel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

@Configuration
@Slf4j
public class GatewayConfiguration {
    public final static String SentinelTestDegradeName = "sentinel_test_degrade";
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    // @Order(-2)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CustomDegradeBlockHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        // return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
        // var r = new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
        return new CustomDegradeBlockHandler(viewResolvers, serverCodecConfigurer);
    }

    // @Bean
    // @Order(-3)
    // public CustomDegradeBlockHandler customDegradeBlockHandler(){
    //     return new CustomDegradeBlockHandler();
    // }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @SuppressWarnings("null")
    @Bean
    public GlobalFilter sentinelGlobalFilter() {
        return (exchange, chain) -> {
            log.info("exec sentinel GlobalFilter");
            return chain.filter(exchange).onErrorResume(BlockException.class, ex -> {
                // 处理熔断降级触发的BlockException
                exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                
                String responseJson = "{\"code\": 429, \"message\": \"请求被限流\"}";
                if (ex instanceof DegradeException) {
                    responseJson = "{\"code\": 503, \"message\": \"服务暂时不可用，已触发熔断降级\"}";
                }
                DataBuffer buffer = exchange.getResponse().bufferFactory()
                        .wrap(responseJson.getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(buffer));
            });
        };
    }

    @PostConstruct
    public void doInit() {
        // initCustomizedApis();
        initGatewayFlowRule();
        initTestResourceDegradeRules();
    }

    @SuppressWarnings("unused")
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition api1 = new ApiDefinition("some_customized_api")
            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                add(new ApiPathPredicateItem().setPattern("/ahas"));
                add(new ApiPathPredicateItem().setPattern("/product/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }});
        ApiDefinition api2 = new ApiDefinition("another_customized_api")
            .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                add(new ApiPathPredicateItem().setPattern("/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }});
        definitions.add(api1);
        definitions.add(api2);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
    private void initTestResourceDegradeRules(){
        List<DegradeRule> rules = new ArrayList<>();
        //1 请求的响应时间大于50 ms则统计为慢调用
        //2 当单位统计时长（statIntervalMs 20000）内请求数目大于设置的最小请求数目（100）
        //3 并且慢调用的比例大于阈值（0.6）
        //4 则接下来的熔断时长（10s）内请求会自动被熔断
        // DegradeRule rule = new DegradeRule(GatewayConfiguration.SentinelTestDegradeName);
        DegradeRule rule = new DegradeRule();
        rule.setResource("resourcerole");
        rule.setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                
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
                .setMinRequestAmount(3)
                .setStatIntervalMs(20000);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
        System.out.println("Degrade rule loaded: " + rules);
    }
    private void initGatewayFlowRule() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        /**
         * rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(1);
         */
        rules.add(new GatewayFlowRule("resourcescope")
            .setGrade(RuleConstant.FLOW_GRADE_QPS)
            .setCount(1)
        );
        // rules.add(new GatewayFlowRule("aliyun_route")
        //     .setCount(2)
        //     .setIntervalSec(2)
        //     .setBurst(2)
        //     .setParamItem(new GatewayParamFlowItem()
        //         .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_CLIENT_IP)
        //     )
        // );
        // rules.add(new GatewayFlowRule("resourcescope")
        //     .setCount(10)
        //     .setIntervalSec(1)
        //     .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
        //     .setMaxQueueingTimeoutMs(600)
        //     .setParamItem(new GatewayParamFlowItem()
        //         .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_HEADER)
        //         .setFieldName("X-Sentinel-Flag")
        //     )
        // );
        // rules.add(new GatewayFlowRule("httpbin_route")
        //     .setCount(1)
        //     .setIntervalSec(1)
        //     .setParamItem(new GatewayParamFlowItem()
        //         .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
        //         .setFieldName("pa")
        //     )
        // );
        // rules.add(new GatewayFlowRule("httpbin_route")
        //     .setCount(2)
        //     .setIntervalSec(30)
        //     .setParamItem(new GatewayParamFlowItem()
        //         .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
        //         .setFieldName("type")
        //         .setPattern("warn")
        //         .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_CONTAINS)
        //     )
        // );

        // rules.add(new GatewayFlowRule("some_customized_api")
        //     .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
        //     .setCount(5)
        //     .setIntervalSec(1)
        //     .setParamItem(new GatewayParamFlowItem()
        //         .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
        //         .setFieldName("pn")
        //     )
        // );
        GatewayRuleManager.loadRules(rules);
    }
}
