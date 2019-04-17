package com.cevs.reactivesocialapp.controllers;

import com.cevs.reactivesocialapp.domain.Review;
import com.cevs.reactivesocialapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private FluxSink<Message<Review>> reviewSink;
    // Message -> Spring abstraction for a POJO wrapped as transportable message
    // that includes the ability to add headers and other information
    private Flux<Message<Review>> flux;

    public ReviewController() {
        this.flux = Flux.<Message<Review>>create(
                emitter -> this.reviewSink = emitter,
                FluxSink.OverflowStrategy.IGNORE
        ).publish().autoConnect();
    }

    @PostMapping("/reviews")
    public Mono<ResponseEntity<?>> addReview(Mono<Review> newReview){
        if(reviewSink != null){
            return newReview
                    .map(review -> {
                        log.info("REVIEW_PROCESSED: " + review.toString());
                        reviewSink.next(
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
