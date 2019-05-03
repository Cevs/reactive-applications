package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.*;
import com.cevs.reactive.shop.dto.ShoppingCartProductDto;
import com.cevs.reactive.shop.repositories.ProductRepository;
import com.cevs.reactive.shop.repositories.ShoppingCartRepository;
import com.cevs.reactive.shop.repositories.UserOrderRepository;
import com.cevs.reactive.shop.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShoppingCartImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final UserOrderRepository userOrderRepository;
    private final Logger log = LoggerFactory.getLogger(ShoppingCartImpl.class);

    public ShoppingCartImpl(ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, UserOrderRepository userOrderRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.userOrderRepository = userOrderRepository;
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

        return monoMap.map(productIdQuantityMap ->  productIdQuantityMap.entrySet())
                .flatMapIterable(entries -> entries)
                .flatMap(productIdQuantityEntry -> {
                    Mono<Product> monoProduct = productRepository.findById(productIdQuantityEntry.getKey());
                    return monoProduct.map(product -> {
                        return new ShoppingCartProductDto(product,productIdQuantityEntry.getValue());
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

    @Override
    public Mono<Void> processOrder() {
        Mono<User> monoUser = ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (User)securityContext.getAuthentication().getPrincipal());

        Mono<Void> emptyCart = monoUser.map(user -> {
            return  user.getId();
        }).flatMap(id ->{
            return shoppingCartRepository.deleteById(id);
        });

        return monoUser.flatMap(user -> {
            Mono<ShoppingCart> monoShoppingCart = shoppingCartRepository.findByUserId(user.getId());
            return monoShoppingCart.map(shoppingCart -> {
                return shoppingCart.getProductIdQuantity().entrySet();
            }).flatMapIterable(entries -> entries)
                    .flatMap(productIdQuantityEntry -> {
                        Mono<Product> monoProduct = productRepository.findById(productIdQuantityEntry.getKey());
                        return monoProduct.map(product -> {
                            return new Order(product, productIdQuantityEntry.getValue());
                        });

                    }).collect(Collectors.toList()).flatMap(orders -> {
                        UserOrder userOrder = new UserOrder(user, orders, LocalDate.now());
                        return userOrderRepository.save(userOrder);
            });
        }).then(emptyCart);

    }
}
