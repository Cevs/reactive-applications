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

@Service
@EnableBinding(Processor.class)
public class ReviewService {

    private final static Logger log = LoggerFactory.getLogger(ReviewService.class);
    private ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @StreamListener
    @Output(Processor.OUTPUT)
    public Flux<Review> save(@Input(Processor.INPUT) Flux<Review> newComments){
        return reviewRepository
                .saveAll(newComments)
                .log("commentService-save")
        .map(review ->{
            log.info("Saving new review " + review);
            return review;
        });
    }
}
