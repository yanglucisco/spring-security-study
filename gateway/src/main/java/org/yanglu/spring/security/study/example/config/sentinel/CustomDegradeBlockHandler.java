package org.yanglu.spring.security.study.example.config.sentinel;

import com.alibaba.csp.sentinel.util.function.Supplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import reactor.core.publisher.Mono;

@Component
public class CustomDegradeBlockHandler
        implements
        WebExceptionHandler
// BlockRequestHandler
{

    private List<ViewResolver> viewResolvers;
    private List<HttpMessageWriter<?>> messageWriters;

    public CustomDegradeBlockHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolvers;
        this.messageWriters = serverCodecConfigurer.getWriters();
    }

    @SuppressWarnings("null")
    private Mono<Void> writeResponse(ServerResponse response, ServerWebExchange exchange) {
        return response.writeTo(exchange, contextSupplier.get());
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 构建清晰的JSON响应体
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("success", false);
        result.put("message", "服务暂时不可用，请稍后再试");
        result.put("timestamp", System.currentTimeMillis());
        result.put("path", exchange.getRequest().getURI().getPath());
        // return Mono.empty();
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE) // 503状态码
                .contentType(MediaType.APPLICATION_JSON) // 确保内容类型为JSON
                .bodyValue(result).flatMap(response -> writeResponse(response, exchange));
    }

    // private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange, Throwable throwable) {
    //     return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    // }

    private final Supplier<ServerResponse.Context> contextSupplier = () -> new ServerResponse.Context() {
        @SuppressWarnings("null")
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return messageWriters;
        }

        @SuppressWarnings("null")
        @Override
        public List<ViewResolver> viewResolvers() {
            return viewResolvers;
        }
    };

    // @SuppressWarnings("null")
    // // @Override
    // public Mono<ServerResponse> handleRequest(ServerWebExchange exchange,
    // Throwable ex) {
    // // 构建清晰的JSON响应体
    // Map<String, Object> result = new HashMap<>();
    // result.put("code", 503);
    // result.put("success", false);
    // result.put("message", "服务暂时不可用，请稍后再试");
    // result.put("timestamp", System.currentTimeMillis());
    // result.put("path", exchange.getRequest().getURI().getPath());

    // return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE) // 503状态码
    // .contentType(MediaType.APPLICATION_JSON) // 确保内容类型为JSON
    // .bodyValue(result);
    // }
}
