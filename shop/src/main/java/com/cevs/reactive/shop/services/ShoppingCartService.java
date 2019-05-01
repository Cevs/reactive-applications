package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.ShoppingCart;
import reactor.core.publisher.Mono;

public interface ShoppingCartService {
    Mono<Void> addItemToCart(Long productId);
    Mono<ShoppingCart> getUserCart();
}
