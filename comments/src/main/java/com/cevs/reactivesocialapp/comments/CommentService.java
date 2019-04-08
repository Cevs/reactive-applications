package com.cevs.reactivesocialapp.comments;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(CustomProcessor.class)
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @StreamListener
    @Output(CustomProcessor.OUTPUT)
    public Flux<Void> save(@Input(CustomProcessor.INPUT) Flux<Comment> newComments){
        return commentRepository
                .saveAll(newComments)
                .log("commentService-save")
                //.thenMany(Mono.empty());
                .flatMap(comment -> Mono.empty());
    }
}
