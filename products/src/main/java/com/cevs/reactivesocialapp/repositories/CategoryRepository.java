package com.cevs.reactivesocialapp.repositories;

import com.cevs.reactivesocialapp.domain.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Flux<Category> findAll();
    Mono<Category> findById(long categoryId);
}
