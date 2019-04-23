package com.cevs.reactive.shop.helpers;

import com.cevs.reactive.shop.domain.Review;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Component
public class CommentHelper {

    private Logger log = LoggerFactory.getLogger(CommentHelper.class);
    private final WebClient webClient;

    public CommentHelper() {
        this.webClient = WebClient.create("http://localhost:9000");
    }

    @HystrixCommand(fallbackMethod = "defaultReviews")
    public Flux<Review> getReviews(long productId){
        return webClient.get()
                .uri("/reviews/{productId}", productId)
                .exchange()
                .flatMapMany(clientResponse -> {
                   return clientResponse.bodyToFlux(Review.class);
                });
    }

    public Flux<Review> defaultReviews(long productId){
        return Flux.empty();
    }
}
