package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.Location;
import com.cevs.reactive.shop.repositories.LocationRepository;
import com.cevs.reactive.shop.services.LocationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Flux<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
