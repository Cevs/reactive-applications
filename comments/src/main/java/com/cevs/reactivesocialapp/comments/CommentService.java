package com.cevs.reactivesocialapp.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(Processor.class)
public class CommentService {

    private final static Logger log = LoggerFactory.getLogger(CommentService.class);
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @StreamListener
    @Output(Processor.OUTPUT)
    public Flux<Comment> save(@Input(Processor.INPUT) Flux<Comment> newComments){
        return commentRepository
                .saveAll(newComments)
                .log("commentService-save")
        .map(comment->{
            log.info("Saving new comment " + comment);
            return comment;
        });
    }
}
