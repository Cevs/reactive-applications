package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.UserReview;
import com.cevs.reactive.shop.domain.Product;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SingleProductService {
    Flux<UserReview> getCompositeProductData(long productId);
    Mono<Product> getProductInfo(long productId);
    Flux<Product> getSimilarProducts(long productId);
    Mono<User> getOwnerOfProduct(long productId);
}
