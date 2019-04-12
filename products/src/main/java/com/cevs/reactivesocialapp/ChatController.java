package com.cevs.reactivesocialapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public Mono<String> chat(){
        return Mono.just("chat");
    }
}
