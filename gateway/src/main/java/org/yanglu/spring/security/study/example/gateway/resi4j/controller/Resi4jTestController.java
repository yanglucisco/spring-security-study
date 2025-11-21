package org.yanglu.spring.security.study.example.gateway.resi4j.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yanglu.spring.security.study.example.gateway.resi4j.service.Resi4jTestService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("rsi4j")
public class Resi4jTestController {
    private final Resi4jTestService resi4jTestService;
    public Resi4jTestController(Resi4jTestService resi4jTestService){
        this.resi4jTestService = resi4jTestService;
    }
    @RequestMapping("testlocalcall")
    public String test(){
        return resi4jTestService.testLocalCall();
    }
    @RequestMapping("testremotecall")
    public Mono<String> testremotecall(
        @RegisteredOAuth2AuthorizedClient("gateway-scope") OAuth2AuthorizedClient authorizedClient
    ){
        return resi4jTestService.testRemoteCall(authorizedClient);
    }
}
