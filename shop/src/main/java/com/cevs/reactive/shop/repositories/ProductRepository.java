package com.cevs.reactive.shop.repositories;

import com.cevs.reactive.shop.domain.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    Mono<Product> findByName(String name);
    Mono<Product> findById(long productId);
    Flux<Product> findAll();
    Flux<Product> findByCategory(String category);
    Mono<Product> findTopByOrderByIdDesc();
}
