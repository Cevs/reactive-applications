package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.Advertisement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface AdvertisementService {
    Flux<Advertisement> getPeriodicallyAdvertisement(long period);
    Flux<Tuple2<Advertisement, Advertisement>> getPeriodicallyAdvertisements(long period);
    Mono<Tuple2<Advertisement, Advertisement>> getInitialAdvertisement();
}
