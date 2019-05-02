package com.cevs.reactive.shop.controllers;

import com.cevs.reactive.shop.domain.ShoppingCart;
import com.cevs.reactive.shop.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;
    private final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    @GetMapping("/shopping-cart")
    public Mono<String> shoppingCart(Model model){
        Mono<ShoppingCart> monoShoppingCart = shoppingCartService.getUserCart();
        model.addAttribute("shoppingCart", monoShoppingCart);
        return Mono.just("shopping-cart");
    }

    @PostMapping("/shopping-cart")
    public Mono<Void> addItemToShoppingCart(@RequestBody String productId){
        return shoppingCartService.addItemToCart(Long.parseLong(productId)).then();
    }

    @DeleteMapping("/shopping-cart/{productId}")
    public Mono<String> removeItemFromCart(@PathVariable String productId){
        return shoppingCartService.removeItemFromCart(Long.parseLong(productId))
                .then(Mono.just("redirect:/shopping-cart"));
    }
}
