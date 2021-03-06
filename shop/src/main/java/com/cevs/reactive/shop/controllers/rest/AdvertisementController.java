package com.cevs.reactive.shop.controllers.rest;

import com.cevs.reactive.shop.domain.Advertisement;
import com.cevs.reactive.shop.helpers.ProductHelper;
import com.cevs.reactive.shop.services.AdvertisementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;

@RestController
public class AdvertisementController
{
    private static final String BASE_PATH = "/advertisement/";
    private static final String FILENAME = "{filename:.+}";
    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService)
    {
        this.advertisementService = advertisementService;
    }

    @GetMapping(
            value = BASE_PATH,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tuple2<Advertisement, Advertisement>> getAdvertisement()
    {
        return advertisementService.getPeriodicallyAdvertisements(2);
    }

    @GetMapping(
            value = BASE_PATH + FILENAME + "/raw",
            produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> getRawImage(@PathVariable String filename)
    {
        return advertisementService
                .findOneAdvertisement(filename)
                .map(resource -> {
                    try
                    {
                        return ResponseEntity
                                .ok()
                                .contentLength(resource.contentLength())
                                .body(new InputStreamResource(
                                        resource.getInputStream()
                                ));
                    }
                    catch (IOException e)
                    {
                        return ResponseEntity
                                .badRequest()
                                .body("Couldn't find " + filename + " => "
                                        + e.getMessage()
                                );
                    }
                });
    }
}
