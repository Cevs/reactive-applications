package com.cevs.reactivesocialapp.comments;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CommentWriterRepository extends ReactiveCrudRepository<Comment, String> {
    Mono<Comment> save(Comment newComment);
    Mono<Comment> findById(String id);
}
