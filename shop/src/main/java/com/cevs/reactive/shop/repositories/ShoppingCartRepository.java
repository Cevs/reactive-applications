package com.cevs.reactive.shop.repositories;

import com.cevs.reactive.shop.domain.ShoppingCart;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ShoppingCartRepository extends ReactiveCrudRepository<ShoppingCart, ObjectId> {
    Mono<ShoppingCart> findByUserId(long userId);
}
