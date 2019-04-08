package com.cevs.reactivesocialapp.comments;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CommentRepository extends ReactiveCrudRepository<Comment, String> {
   Flux<Comment> findByImageId(String imageId);
   Flux<Comment> saveAll(Flux<Comment> newComment);
   Mono<Comment> findById(String id);
   Mono<Void> deleteAll();
}
