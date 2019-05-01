package com.cevs.reactive.shop.controllers;

import com.cevs.reactive.shop.domain.ShoppingCart;
import com.cevs.reactive.shop.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Controller
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

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
}
