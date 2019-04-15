package com.cevs.reactivesocialapp.comments;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveCrudRepository<Review, String> {
   Flux<Review> findByProductId(String productId);
   Flux<Review> saveAll(Flux<Review> newComment);
   Mono<Review> findById(String id);
   Mono<Void> deleteAll();
}
