package com.cevs.reactivesocialapp.comments.controllers;

import com.cevs.reactivesocialapp.comments.domain.Review;
import com.cevs.reactivesocialapp.comments.repositories.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class ReviewController {

    private final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/reviews/{productId}")
    public Flux<Review> comments(@PathVariable long productId){
        return reviewRepository.findByProductId(productId).map(review -> {
                log.info("FETCH REVIEW: " + review.toString());
                return review;
        });
    }
}
