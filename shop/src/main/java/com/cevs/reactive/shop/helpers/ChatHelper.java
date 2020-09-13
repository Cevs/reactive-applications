package com.cevs.reactive.shop.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ChatHelper {

    private final WebClient webClient;

    public ChatHelper() {
        this.webClient = WebClient.create("http://localhost:8200");
    }

    public Mono<Void> createUserChatStore(String username){
        return webClient.post()
                .uri("/chat/store")
                .body(BodyInserters.fromObject(username))
                .exchange()
                .then();
    }
}
