package com.cevs.reactive.shop.repositories;

import com.cevs.reactive.shop.domain.Advertisement;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AdvertisementRepository extends ReactiveCrudRepository<Advertisement, Long> {
    Flux<Advertisement> findAll();
    Mono<Advertisement> findById(long advertisementId);
    Mono<Long>  count();
}
