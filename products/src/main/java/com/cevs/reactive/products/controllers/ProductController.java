package com.cevs.reactivesocialappproducts.controllers;

import com.cevs.reactivesocialappproducts.domain.Product;
import com.cevs.reactivesocialappproducts.dto.ProductDto;
import com.cevs.reactivesocialappproducts.repositories.ProductRepository;
import com.cevs.reactivesocialappproducts.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;

@RestController
public class ProductController {
    private final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private static final String FILENAME = "{filename:.+}";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public Flux<Product> getAllProducts(){
        return productService.findAllProducts();
    }

    @GetMapping("/product/{productId}")
    public Mono<Product> getProduct(@PathVariable long productId){
        return productService.getProduct(productId);
    }

    @GetMapping("/product/"+FILENAME+"/raw")
    public Mono<Resource> getProductResource(@PathVariable String filename){
        return productService.findOneProduct(filename);
    }

    @PostMapping("/product/new")
    public Mono<Void> insertProduct(ProductDto productDto){
        return productService.insertProduct(productDto);
    }

    @DeleteMapping("/product/{productId}")
    public Mono<Void> deleteProduct(@PathVariable long productId){
        return productService.deleteProduct(productId);
    }

    @PutMapping("/product/update")
    public Mono<Void> updateProduct(ProductDto productDto){
        return productService.updateProduct(productDto);
    }
}
