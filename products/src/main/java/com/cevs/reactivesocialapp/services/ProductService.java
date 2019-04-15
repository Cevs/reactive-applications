package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.Product;
import com.cevs.reactivesocialapp.dto.ProductDto;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> findAllProducts();
    Mono<Resource> findOneProduct(String filename);
    Mono<Void> insertProduct(ProductDto product);
    Mono<Void> deleteProduct(String filename);
}
