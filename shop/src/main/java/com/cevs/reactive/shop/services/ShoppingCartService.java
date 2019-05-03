package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.dto.ShoppingCartProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ShoppingCartService {
    Mono<Void> addItemToCart(Long productId);
    Flux<ShoppingCartProductDto> getUserCart();
    Mono<Void> removeItemFromCart(Long productId);
    Mono<Void> processOrder();
}
