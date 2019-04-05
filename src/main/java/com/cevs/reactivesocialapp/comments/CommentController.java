package com.cevs.reactivesocialapp.comments;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class CommentController {

    private final RabbitTemplate rabbitTemplate;
    private final CommentWriterRepository commentRepository;

    public CommentController(RabbitTemplate rabbitTemplate, CommentWriterRepository commentRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment){
        return newComment
                .flatMap(comment ->
                    Mono.fromRunnable(()-> rabbitTemplate
                    .convertAndSend(
                            "learning-reactive",
                            "comments.new",
                            comment
                    )))
                .log("commentService-publish")
                .then(Mono.just("redirect:/"));
    }
}
