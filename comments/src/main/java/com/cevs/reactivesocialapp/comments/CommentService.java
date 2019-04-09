package com.cevs.reactivesocialapp.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger log = LoggerFactory.getLogger(CustomProcessor.class);
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @StreamListener
    @Output(CustomProcessor.OUTPUT)
    public Flux<Comment> save(@Input(CustomProcessor.INPUT) Flux<Comment> newComments){
        return commentRepository
                .saveAll(newComments)
                .log("commentService-save")
                //.thenMany(Mono.empty());
                //.flatMap(comment -> Mono.empty());
        .map(comment->{
            log.info("Saving new comment " + comment);
            return comment;
        });
    }
}
