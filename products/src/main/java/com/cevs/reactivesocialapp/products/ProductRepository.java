package com.cevs.reactivesocialapp.products;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    Mono<Product> findByName(String name);
    Flux<Product> findAll();
}
