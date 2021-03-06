package com.cevs.reactive.products.controllers;

import com.cevs.reactive.products.domain.Product;
import com.cevs.reactive.products.dto.ProductDto;
import com.cevs.reactive.products.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;

@RestController
public class ProductController {
    private final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private static final String FILENAME = "{filename:.+}";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/search")
    public Flux<Product> getProducts(
            @RequestParam("owner") String username,
            @RequestParam("productName") String productName,
            @RequestParam("productCategory") String productCategory,
            @RequestParam("productLocation") String productLocation,
            @RequestParam(name="priceLowerLimit", defaultValue = "0") double lowerLimit,
            @RequestParam(name="priceUpperLimit", defaultValue = "0") double upperLimit){

        if(username != ""){
            if(lowerLimit == 0 || upperLimit == 0){
                return productService.findProductsBySearchCriteriaNotOwnedByUser(
                        productName,productCategory,productLocation, username
                );
            }
            else{
                return productService.findProductsBySearchCriteriaInsidePriceRangeNotOwnedByUser(
                        productName,productCategory,productLocation,lowerLimit,upperLimit, username
                );
            }
        }
        else{
            if(lowerLimit == 0 || upperLimit == 0){
                return productService.findProductsBySearchCriteria(
                        productName,productCategory,productLocation, username
                );
            }
            else{
                return productService.findProductsBySearchCriteriaInsidePriceRange(
                        productName,productCategory,productLocation,lowerLimit,upperLimit, username
                );
            }
        }
    }

    @GetMapping("/products/owner/search")
    public Flux<Product> getOwnerProducts(
            @RequestParam("owner") String username,
            @RequestParam("productName") String productName,
            @RequestParam("productCategory") String productCategory,
            @RequestParam("productLocation") String productLocation,
            @RequestParam(name="priceLowerLimit", defaultValue = "0") double lowerLimit,
            @RequestParam(name="priceUpperLimit", defaultValue = "0") double upperLimit){

            if(lowerLimit == 0 || upperLimit == 0){
                return productService.findProductsBySearchCriteria(
                        productName,productCategory,productLocation, username
                );
            }
            else{
                return productService.findProductsBySearchCriteriaInsidePriceRange(
                        productName,productCategory,productLocation,lowerLimit,upperLimit, username
                );
            }

    }

    @GetMapping("/products")
    public Flux<Product> getAllProducts(@RequestParam("username") String username)
    {
        if(username.isEmpty()){
            return productService.findAllProducts();
        }else{
            return productService.findAllNotOwnedProducts(username);
        }
    }

    @GetMapping("/products/own")
    public Flux<Product> getAllProductsOwnedByUser(@RequestParam("username")String username){
        return productService.findAllProductsOwnedBy(username);
    }

    @GetMapping("/product/{productId}")
    public Mono<Product> getProduct(@PathVariable long productId){
        return productService.getProduct(productId);
    }

    @GetMapping("/product/"+FILENAME+"/raw")
    public Mono<Resource> getProductResource(@PathVariable String filename){
        return productService.findOneProduct(filename);
    }

    @PostMapping("/product/new")
    public Mono<Void> insertProduct(ProductDto productDto){
        return productService.insertProduct(productDto);
    }

    @DeleteMapping("/product/{productId}")
    public Mono<Void> deleteProduct(@PathVariable long productId){
        return productService.deleteProduct(productId);
    }

    @PutMapping("/product/update")
    public Mono<Void> updateProduct(ProductDto productDto){
        return productService.updateProduct(productDto);
    }
}
