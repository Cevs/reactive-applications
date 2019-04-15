package com.cevs.reactivesocialapp.controllers;


import com.cevs.reactivesocialapp.dto.UserReview;
import com.cevs.reactivesocialapp.products.Product;
import com.cevs.reactivesocialapp.services.SingleProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class SingleProductController {

    @Autowired
    SingleProductService singleProductService;

    private static final String FILENAME = "{filename:.+}";

    @GetMapping("/product/{productId}")
    public Mono<String> product(@PathVariable String productId, Model model){

        Flux<UserReview> flux = singleProductService.getCompositeProductData(productId);
        Mono<Product> monoProduct = singleProductService.getProductInfo(productId);

        IReactiveDataDriverContextVariable reactiveDataDrivenMode =
                new ReactiveDataDriverContextVariable(flux
                        .map(data-> new HashMap<String, Object>(){{
                            put("user",data.getUser());
                            put("review", data.getReview());
                        }})
                        ,1);

        model.addAttribute("userReviews", reactiveDataDrivenMode);
        model.addAttribute("product", monoProduct);

        return Mono.just("product");
    }

    //produces = MediaType.IMAGE_JPEG_VALUE => Tell browser to render image (set the Content-Type header)
    @GetMapping(value = "product/" + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename){
        return singleProductService.findOneProduct(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok().contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    }catch(IOException e){
                        return ResponseEntity.badRequest().body("Couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }
}
