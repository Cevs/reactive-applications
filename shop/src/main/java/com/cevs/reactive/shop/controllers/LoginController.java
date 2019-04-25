package com.cevs.reactive.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public Mono<String> login(){
        return Mono.just("login");
    }
}
