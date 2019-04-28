package com.cevs.reactive.products.services;

import com.cevs.reactive.products.domain.Product;
import com.cevs.reactive.products.dto.ProductDto;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> findAllProducts();
    Flux<Product> findAllNotOwnedProducts(String username);
    Flux<Product> findAllProductsOwnedBy(String username);
    Flux<Product> findProductsBySearchNameAndCategoryAndLocationAndPriceRange(
            String productName, String category, String location, double lowerLimit, double upperLimit
    );
    Flux<Product> findProductsBySearchNameAndCategoryAndLocation(
            String productName, String category, String location
    );
    Mono<Resource> findOneProduct(String filename);
    Mono<Void> insertProduct(ProductDto product);
    Mono<Void> deleteProduct(long productId);
    Mono<Product> getProduct(long productId);
    Mono<Void> updateProduct(ProductDto productDto);
}
