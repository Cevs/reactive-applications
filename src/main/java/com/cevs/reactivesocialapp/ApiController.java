package com.cevs.reactivesocialapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private static final String API_BASE_PATH = "/api";

    @GetMapping(API_BASE_PATH + "/images")
    Flux<Image> images(){
        return Flux.just(
                new Image("1", "Image 1"),
                new Image("2", "Image 2"),
                new Image("3", "Image 3")
        );
    }

    @PostMapping(API_BASE_PATH + "/images")
    Mono<Void> create(@RequestBody Flux<Image> images){
        return images
                //To consume data we map over it (we simply log and pass the original image ono the next step of the flow)
                .map(image -> {
                    log.info("Saving image " + image.getName() + " to reactive databse");
                    return image;
                })
                .then();
    }
}
