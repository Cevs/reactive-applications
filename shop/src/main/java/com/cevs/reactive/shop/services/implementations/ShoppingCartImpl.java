package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.Product;
import com.cevs.reactive.shop.domain.ShoppingCart;
import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.ShoppingCartProductDto;
import com.cevs.reactive.shop.repositories.ProductRepository;
import com.cevs.reactive.shop.repositories.ShoppingCartRepository;
import com.cevs.reactive.shop.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShoppingCartImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(ShoppingCartImpl.class);

    public ShoppingCartImpl(ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
    }


    @Override
    public Mono<Void> addItemToCart(Long productId) {
        Mono<Product> monoProduct = productRepository.findById(productId);

        Mono<Long> monoUserId = ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (User)securityContext.getAuthentication().getPrincipal())
                .map(user -> {return user.getId();});


        Mono<ShoppingCart> monoShoppingCart = monoUserId.flatMap(id->{
            return shoppingCartRepository.findByUserId(id).map(shoppingCart -> {
                return shoppingCart;
            }).defaultIfEmpty(new ShoppingCart(id));
        });

        return monoProduct.zipWith(monoShoppingCart).map(objects -> {
            Map<Long, Long> productIdQuantity = objects.getT2().getProductIdQuantity();
            long productQuantity = 0;
            if(productIdQuantity.get(objects.getT1().getId()) != null){
                productQuantity = productIdQuantity.get(objects.getT1().getId());
            }
            long newProductQuantity = productQuantity + 1;
            productIdQuantity.put(objects.getT1().getId(), newProductQuantity);
            return objects.getT2();
        }).flatMap(shoppingCart -> {
            return Mono.defer(() -> {
                return shoppingCartRepository.save(shoppingCart);
            });
        }).then();
    }

    @Override
    public Flux<ShoppingCartProductDto> getUserCart() {
        Mono<Long> monoUserId = ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (User)securityContext.getAuthentication().getPrincipal())
                .map(user -> {return user.getId();});

        Mono<Map<Long, Long>> monoMap = monoUserId.flatMap(id->{
             return shoppingCartRepository.findByUserId(id).map(shoppingCart -> {
                 return shoppingCart.getProductIdQuantity();
             });
        });

        return monoMap.map(longLongMap ->  longLongMap.entrySet())
                .flatMapIterable(entries -> entries)
                .flatMap(longLongEntry -> {
                    Mono<Product> monoProduct = productRepository.findById(longLongEntry.getKey());
                    return monoProduct.map(product -> {
                        return new ShoppingCartProductDto(product,longLongEntry.getValue());
                    });
                });
    }

    @Override
    public Mono<Void> removeItemFromCart(Long productId) {

        Mono<Long> monoUserId = ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (User)securityContext.getAuthentication().getPrincipal())
                .map(user -> {return user.getId();});

        Mono<ShoppingCart> monoShoppingCart = monoUserId.flatMap(userId -> {
            return shoppingCartRepository.findByUserId(userId);
        });

        return monoShoppingCart.flatMap(shoppingCart -> {
           Map<Long, Long> mapProductIdQuantity = shoppingCart.getProductIdQuantity();
           Long quantity = mapProductIdQuantity.get(productId);
           if(quantity > 1){
               Long newQuantity = quantity-1;
               mapProductIdQuantity.put(productId,newQuantity);
               shoppingCart.setProductIdQuantity(mapProductIdQuantity);
           }else{
               mapProductIdQuantity.remove(productId);
               shoppingCart.setProductIdQuantity(mapProductIdQuantity);
            }
            return shoppingCartRepository.save(shoppingCart);
        }).then();
    }
}
