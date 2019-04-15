package com.cevs.reactivesocialapp.comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class ReviewController {

    private final Logger log = LoggerFactory.getLogger("CommentController");
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/comments/{productId}")
    public Flux<Review> comments(@PathVariable String productId){
        return reviewRepository.findByProductId(productId).map(review -> {
                log.info("FETCH COMMENT: " + review.toString());
                return review;
        });
    }
}
