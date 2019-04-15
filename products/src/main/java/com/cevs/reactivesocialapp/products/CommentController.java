package com.cevs.reactivesocialapp.products;

import com.cevs.reactivesocialapp.domain.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;


@RestController
@EnableBinding(Source.class)
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private FluxSink<Message<Review>> commentSink;
    // Message -> Spring abstraction for a POJO wrapped as transportable message
    // that includes the ability to add headers and other information
    private Flux<Message<Review>> flux;

    public CommentController() {
        this.flux = Flux.<Message<Review>>create(
                emitter -> this.commentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE
        ).publish().autoConnect();
    }

    @PostMapping("/comments")
    public Mono<ResponseEntity<?>> addComment(Mono<Review> newComment){
        if(commentSink != null){
            return newComment
                    .map(review -> {
                        commentSink.next(
                                MessageBuilder
                                        .withPayload(review)
                                        .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build());
                        return review;
                    })
                    .flatMap(review -> {
                        return Mono.just(ResponseEntity.noContent().build()); //return HTTP 204(No Content) -> Indicate success
                    });
        }else{
            return Mono.just(ResponseEntity.noContent().build());
        }
    }

    @StreamEmitter
    public void emit(@Output(Source.OUTPUT) FluxSender output){
        output.send(this.flux);
    }
}
