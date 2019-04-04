package com.cevs.reactivesocialapp.repositories;

import com.cevs.reactivesocialapp.domain.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageRepository extends ReactiveCrudRepository<Image, String> {
    Mono<Image> findByName(String name);
    Flux<Image> findAll();
}
