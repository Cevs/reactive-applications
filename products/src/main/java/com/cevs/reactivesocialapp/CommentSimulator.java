package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.domain.Review;
import com.cevs.reactivesocialapp.products.CommentController;
import com.cevs.reactivesocialapp.repositories.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class CommentSimulator {

    private final CommentController commentController;
    private final ProductRepository productRepository;

    private final AtomicInteger counter;

    public CommentSimulator(CommentController commentController, ProductRepository productRepository) {
        this.commentController = commentController;
        this.productRepository = productRepository;
        this.counter = new AtomicInteger(1);
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event){
        Flux
                .interval(Duration.ofMillis(5000))
                .flatMap(tick-> productRepository.findAll())
                .map(image -> {
                    Review review = new Review();
                    review.setProductId(image.getId());
                    review.setComment("Review #" + counter.getAndIncrement());
                    return Mono.just(review);
                })
                .flatMap(newCommnet->
                        Mono.defer(()->commentController.addComment(newCommnet)))
        .subscribe();
    }
}
