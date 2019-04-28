package com.cevs.reactive.shop.controllers;


import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.UserReview;
import com.cevs.reactive.shop.domain.Product;
import com.cevs.reactive.shop.helpers.ProductHelper;
import com.cevs.reactive.shop.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class SingleProductPageController {

    @Autowired
    SingleProductService singleProductService;
    @Autowired
    AdvertisementService advertisementService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    LocationService locationService;
    @Autowired
    UserService userService;

    private final ProductHelper productHelper;

    private static final Logger log = LoggerFactory.getLogger(SingleProductPageController.class);

    public SingleProductPageController(ProductHelper productHelper) {
        this.productHelper = productHelper;
    }

    @GetMapping("/product/{productId}")
    public Mono<String> product(@PathVariable long productId, Model model){

        Flux<UserReview> fluxUserReview = singleProductService.getCompositeProductData(productId);
        Mono<Product> monoProduct = singleProductService.getProductInfo(productId);
        Flux<Product> fluxSimilarProduct = singleProductService.getSimilarProducts(productId);
        Mono<User> monoUser = singleProductService.getOwnerOfProduct(productId);

        IReactiveDataDriverContextVariable reactiveFluxSimilarProducts =
                new ReactiveDataDriverContextVariable(fluxSimilarProduct,1);

        model.addAttribute("userReviews", fluxUserReview);
        model.addAttribute("product", monoProduct);
        model.addAttribute("similarProducts", reactiveFluxSimilarProducts);
        model.addAttribute("initialAdvertisements",
                advertisementService.getInitialAdvertisement()
                .map(objects -> {
                    return new HashMap<String, String>(){{
                        put("primaryImage", objects.getT1().getImageName());
                        put("secondaryImage" , objects.getT2().getImageName());
                    }};
                })
        );
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("user", monoUser);


        return Mono.just("product");
    }


    @DeleteMapping(value = "/product/{productId}")
    public Mono<String> deleteProduct(@PathVariable long productId){
        return productHelper.deleteProduct(productId).then(Mono.just("redirect:/"));
    }
}
