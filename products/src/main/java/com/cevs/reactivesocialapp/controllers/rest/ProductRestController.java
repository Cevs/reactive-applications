package com.cevs.reactivesocialapp.controllers.rest;

import com.cevs.reactivesocialapp.dto.ProductDto;
import com.cevs.reactivesocialapp.products.Product;
import com.cevs.reactivesocialapp.products.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ProductRestController {

    private static final String BASE_PATH = "/products";

    private final ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(ProductRestController.class);

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

   //TODO

}
