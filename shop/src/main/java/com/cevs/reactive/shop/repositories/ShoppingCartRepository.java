package com.cevs.reactive.shop.repositories;

import com.cevs.reactive.shop.domain.ShoppingCart;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ShoppingCartRepository extends ReactiveCrudRepository<ShoppingCart, Long> {
    Mono<ShoppingCart> findByUserId(long userId);
}
