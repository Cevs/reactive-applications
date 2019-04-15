package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.dto.UserReview;
import com.cevs.reactivesocialapp.products.Product;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SingleProductService {
    Flux<UserReview> getCompositeProductData(String productId);
    Mono<Product> getProductInfo(String productId);
    Mono<Resource> findOneProduct(String filename);
}
