package com.cevs.reactive.shop.repositories;

import com.cevs.reactive.shop.domain.Location;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LocationRepository extends ReactiveCrudRepository<Location, Long> {
    Flux<Location> findAll();
}
