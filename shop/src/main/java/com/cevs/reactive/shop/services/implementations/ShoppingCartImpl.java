package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.Product;
import com.cevs.reactive.shop.domain.ShoppingCart;
import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.repositories.ProductRepository;
import com.cevs.reactive.shop.repositories.ShoppingCartRepository;
import com.cevs.reactive.shop.services.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

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
                log.info(shoppingCart.getUserId()+"");
                return shoppingCart;
            }).defaultIfEmpty(new ShoppingCart(id));
        });

        return monoProduct.zipWith(monoShoppingCart).map(objects -> {
            List<Product> products = objects.getT2().getProducts();
            products.add(objects.getT1());
            return objects.getT2();
        }).flatMap(shoppingCart -> {
            return Mono.defer(() -> {
                return shoppingCartRepository.save(shoppingCart);
            });
        }).then();
    }

    @Override
    public Mono<ShoppingCart> getUserCart() {
        Mono<Long> monoUserId = ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (User)securityContext.getAuthentication().getPrincipal())
                .map(user -> {return user.getId();});

        return monoUserId.flatMap(id->{
           return shoppingCartRepository.findByUserId(id);
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
            shoppingCart.getProducts().removeIf(product -> product.getId() == productId );
            return shoppingCartRepository.save(shoppingCart);
        }).then();
    }
}
