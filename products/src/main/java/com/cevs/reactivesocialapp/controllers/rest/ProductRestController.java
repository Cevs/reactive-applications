package com.cevs.reactivesocialapp.controllers.rest;

import com.cevs.reactivesocialapp.products.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

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
