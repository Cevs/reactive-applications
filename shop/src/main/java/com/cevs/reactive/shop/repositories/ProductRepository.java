package com.cevs.reactivesocialapp.repositories;

import com.cevs.reactivesocialapp.domain.Product;
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
