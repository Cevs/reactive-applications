package com.cevs.reactivesocialapp.images;

import com.cevs.reactivesocialapp.comments.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CommentReaderRepository extends ReactiveCrudRepository<Comment, String> {
    Flux<Comment> findByImageId(String imageId);
}
