package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.Advertisement;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface AdvertisementService {
    Mono<Resource> findOneAdvertisement(String filename);
    Flux<Advertisement> getPeriodicallyAdvertisement(long period);
    Flux<Tuple2<Advertisement, Advertisement>> getPeriodicallyAdvertisements(long period);
    Mono<Tuple2<Advertisement, Advertisement>> getInitialAdvertisement();
}
