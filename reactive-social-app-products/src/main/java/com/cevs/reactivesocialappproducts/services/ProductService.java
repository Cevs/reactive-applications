package com.cevs.reactivesocialappproducts.services;

import com.cevs.reactivesocialappproducts.domain.Product;
import com.cevs.reactivesocialappproducts.dto.ProductDto;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> findAllProducts();
    Mono<Resource> findOneProduct(String filename);
    Mono<Void> insertProduct(ProductDto product);
    Mono<Void> deleteProduct(long productId);
    Mono<Product> getProduct(long productId);
    Mono<Void> updateProduct(ProductDto productDto);
}
