package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.Advertisement;
import com.cevs.reactivesocialapp.repositories.AdvertisementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private static final Logger log = LoggerFactory.getLogger(AdvertisementServiceImpl.class);
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public Flux<Advertisement> getPeriodicallyAdvertisement(long period) {
        Flux<Long> fluxInterval = Flux.interval(Duration.ofSeconds(period));

        return fluxInterval.flatMap(interval->{
            Mono<Long> advertisementCount = advertisementRepository.count();
            Mono<Advertisement> advertisement  = advertisementCount.map(count->{
                return (interval % count)+1;
            }).flatMap(advertisementId ->{
                return advertisementRepository.findById(advertisementId);
            });
            return advertisement;
        });
    }

    @Override
    public Flux<Tuple2<Advertisement, Advertisement>> getPeriodicallyAdvertisements(long period) {
        Flux<Long> fluxInterval = Flux.interval(Duration.ofSeconds(period));

        Flux<Advertisement> fluxAdvertisementPrimary =
                fluxInterval.flatMap(interval->{
                    Mono<Long> advertisementCount = advertisementRepository.count();
                    Mono<Advertisement> advertisement  = advertisementCount.map(count->{
                        return (interval % count)+1;
                    }).flatMap(advertisementId ->{
                        return advertisementRepository.findById(advertisementId).map(c->c);
                    });
                    return advertisement;
                });

        Flux<Advertisement> fluxAdvertisementSecondary =
                fluxInterval.flatMap(interval->{
                    Mono<Long> advertisementCount = advertisementRepository.count();
                    Mono<Advertisement> advertisement  = advertisementCount.map(count->{
                        return ((interval % count)+2>count)?1:(interval % count)+2;
                    }).flatMap(advertisementId ->{
                        return advertisementRepository.findById(advertisementId).map(c ->c);
                    });
                    return advertisement;
                });

        return Flux.zip(fluxAdvertisementPrimary,fluxAdvertisementSecondary);
    }

    @Override
    public Mono<Tuple2<Advertisement, Advertisement>> getInitialAdvertisement() {
        Mono<Advertisement> monoPrimaryAdvertisement = advertisementRepository.findById(1);
        Mono<Advertisement> monoSecondaryAdvertisement = advertisementRepository.findById(2);

        return Mono.zip(monoPrimaryAdvertisement, monoSecondaryAdvertisement);
    }
}
