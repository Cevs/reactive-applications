package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.images.Comment;
import com.cevs.reactivesocialapp.images.CommentController;
import com.cevs.reactivesocialapp.images.ImageRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class CommentSimulator {

    private final CommentController commentController;
    private final ImageRepository imageRepository;

    private final AtomicInteger counter;

    public CommentSimulator(CommentController commentController, ImageRepository imageRepository) {
        this.commentController = commentController;
        this.imageRepository = imageRepository;
        this.counter = new AtomicInteger(1);
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event){
        Flux
                .interval(Duration.ofMillis(5000))
                .flatMap(tick-> imageRepository.findAll())
                .map(image -> {
                    Comment comment = new Comment();
                    comment.setImageId(image.getId());
                    comment.setComment("Comment #" + counter.getAndIncrement());
                    return Mono.just(comment);
                })
                .flatMap(newCommnet->
                        Mono.defer(()->commentController.addComment(newCommnet)))
        .subscribe();
    }
}
