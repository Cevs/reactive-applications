package com.cevs.reactive.shop.controllers;

import com.cevs.reactive.shop.helpers.ProductHelper;
import com.cevs.reactive.shop.services.CategoryService;
import com.cevs.reactive.shop.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class HomeController {

    private static final String BASE_PATH = "/products";
    private static final String FILENAME = "{filename:.+}";

    private final CategoryService categoryService;
    private final ProductHelper productHelper;
    private final LocationService locationService;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    public HomeController(CategoryService categoryService, ProductHelper productHelper, LocationService locationService) {
        this.categoryService = categoryService;
        this.productHelper = productHelper;
        this.locationService = locationService;
    }

    /*
        Generating the HTTP OK/HTTP BAD REQUEST response doesn't happen until map() is executed.
        This is chained to the image service fetching the file from disk.
        And none od that happens until the client subscribes.
        Subscribing is handled by framework when the request comes in.
     */
    //produces = MediaType.IMAGE_JPEG_VALUE => Tell browser to render image (set the Content-Type header)
    @GetMapping(value = BASE_PATH + "/" + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename){
        return productHelper.getProductResource(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok().contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    }catch(IOException e){
                        return ResponseEntity.badRequest().body("Couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }

    @GetMapping("/")
    public Mono<String> index(Model model) {

        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(productHelper.getAllProducts()
                        .map(product-> new HashMap<String, Object>(){{
                            log.info(product.toString());
                            put("id", product.getId());
                            put("name", product.getName());
                            put("description", product.getDescription());
                            put("imageName", product.getImageName());
                            put("category", product.getCategory());
                            put("price", product.getPrice());
                        }}),1);

        model.addAttribute("products", reactiveDataDrivenMode);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("locations", locationService.getAllLocations());
        return Mono.just("index");
    }
}
