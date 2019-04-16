package com.cevs.reactivesocialapp.controllers.rest;

import com.cevs.reactivesocialapp.domain.Advertisement;
import com.cevs.reactivesocialapp.services.AdvertisementService;
import com.cevs.reactivesocialapp.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdvertisementRest {

    @Autowired
    AdvertisementService advertisementService;
    @Autowired
    ProductService productService;

    private static final String BASE_PATH = "/advertisement/";
    private static final String FILENAME = "{filename:.+}";
    private static final Logger log = LoggerFactory.getLogger(AdvertisementRest.class);


    @GetMapping(value =  BASE_PATH, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tuple2<Advertisement, Advertisement>> getAdvertisement(){
        return advertisementService.getPeriodicallyAdvertisements(2);
    }

    @GetMapping(value = BASE_PATH  + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename){
        return
                productService.findOneProduct(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok().contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    }catch(IOException e){
                        return ResponseEntity.badRequest().body("Couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }
}
