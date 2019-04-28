package com.cevs.reactive.shop.controllers;

import com.cevs.reactive.shop.helpers.ProductHelper;
import com.cevs.reactive.shop.services.CategoryService;
import com.cevs.reactive.shop.services.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Controller
public class UserProductsController {

    private final ProductHelper productHelper;
    private final CategoryService categoryService;
    private final LocationService locationService;

    public UserProductsController(ProductHelper productHelper, CategoryService categoryService, LocationService locationService) {
        this.productHelper = productHelper;
        this.categoryService = categoryService;
        this.locationService = locationService;
    }

    @GetMapping("/my-products")
    public Mono<String> getUserProducts(Model model){
        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable( productHelper.getUserProducts()
                        .map(product-> new HashMap<String, Object>(){{
                            put("id", product.getId());
                            put("name", product.getName());
                            put("description", product.getDescription());
                            put("imageName", product.getImageName());
                            put("category", product.getCategory());
                            put("price", product.getPrice());
                        }})
                        ,1);

        model.addAttribute("products", reactiveDataDrivenMode);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("locations", locationService.getAllLocations());
        return Mono.just("my-products");
    }
}
