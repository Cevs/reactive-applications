package com.cevs.reactive.shop.controllers;

import com.cevs.reactive.shop.domain.ShoppingCart;
import com.cevs.reactive.shop.dto.ShoppingCartProductDto;
import com.cevs.reactive.shop.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.math.MathFlux;

@Controller
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;
    private final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    @GetMapping("/shopping-cart")
    public Mono<String> shoppingCart(Model model){
        Flux<ShoppingCartProductDto> fluxProduct = shoppingCartService.getUserCart();
        model.addAttribute("productQuantity", fluxProduct);
        model.addAttribute("total",
                MathFlux.sumDouble(
                        fluxProduct.map(shoppingCartProductDto -> {
                            return shoppingCartProductDto.getProduct().getPrice() * shoppingCartProductDto.getQuantity();
                        }).defaultIfEmpty(0.0)
                ));
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

    @PostMapping("/shopping-cart/order")
    public Mono<String> processOrder(){
        return shoppingCartService.processOrder().then(Mono.just("redirect:/shopping-cart"));
    }
}
