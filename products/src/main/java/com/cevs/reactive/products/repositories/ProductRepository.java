package com.cevs.reactive.products.repositories;

import com.cevs.reactive.products.domain.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    Mono<Product> findByName(String name);
    Mono<Product> findById(long productId);
    Flux<Product> findAll();
    Mono<Product> findTopByOrderByIdDesc();
    Flux<Product> findByNameContainsAndCategoryContainsAndLocationNameContainsAndPriceBetween(
            String productName, String category, String location, double lowerLimit, double upperLimit
    );
    Flux<Product> findByNameContainsAndCategoryContainsAndLocationNameContains(String productName, String category, String location);
}
