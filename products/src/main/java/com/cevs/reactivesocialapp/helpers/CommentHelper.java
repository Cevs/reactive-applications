package com.cevs.reactivesocialapp.helpers;

import com.cevs.reactivesocialapp.domain.Review;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
