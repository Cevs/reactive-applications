package com.cevs.reactivesocialapp.controllers;


import com.cevs.reactivesocialapp.dto.ProductInfoDto;
import com.cevs.reactivesocialapp.dto.UserReview;
import com.cevs.reactivesocialapp.domain.Product;
import com.cevs.reactivesocialapp.services.AdvertisementService;
import com.cevs.reactivesocialapp.services.SingleProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    AdvertisementService advertisementService;

    private static final String FILENAME = "{filename:.+}";
    private static final Logger log = LoggerFactory.getLogger(SingleProductController.class);

    @GetMapping("/product/{productId}")
    public Mono<String> product(@PathVariable long productId, Model model){

        Flux<UserReview> fluxUserReview = singleProductService.getCompositeProductData(productId);
        Mono<ProductInfoDto> monoProduct = singleProductService.getProductInfo(productId);
        Flux<ProductInfoDto> fluxSimilarProduct = singleProductService.getSimilarProducts(productId);

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
