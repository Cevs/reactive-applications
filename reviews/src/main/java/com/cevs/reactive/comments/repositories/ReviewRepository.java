package com.cevs.reactive.comments.repositories;

import com.cevs.reactive.comments.domain.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveCrudRepository<Review, String> {
   Flux<Review> findByProductId(long productId);
   Flux<Review> saveAll(Flux<Review> newComment);
   //Mono<Review> findById(String id);
   Mono<Void> deleteAll();
}
