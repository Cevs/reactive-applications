package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.Location;
import reactor.core.publisher.Flux;

public interface LocationService {
    public Flux<Location> getAllLocations();
}
