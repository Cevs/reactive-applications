package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.dto.ProductInfoDto;
import com.cevs.reactivesocialapp.dto.UserReview;
import com.cevs.reactivesocialapp.domain.Product;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SingleProductService {
    Flux<UserReview> getCompositeProductData(long productId);
    Mono<ProductInfoDto> getProductInfo(long productId);
    Mono<Resource> findOneProduct(String filename);
    Flux<ProductInfoDto> getSimilarProducts(long productId);
}
