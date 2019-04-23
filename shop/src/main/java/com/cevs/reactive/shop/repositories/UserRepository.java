package com.cevs.reactive.shop.repositories;

import com.cevs.reactive.shop.domain.User;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, ObjectId> {
    Mono<User> findById(long userId);
}
