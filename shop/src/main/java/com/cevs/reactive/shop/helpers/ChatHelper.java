package com.cevs.reactive.shop.helpers;

import com.cevs.reactive.shop.domain.Review;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
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
