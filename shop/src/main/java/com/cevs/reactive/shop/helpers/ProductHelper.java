package com.cevs.reactive.shop.helpers;


import com.cevs.reactive.shop.domain.Product;
import com.cevs.reactive.shop.domain.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;


@Component
public class ProductHelper {
    private Logger log = LoggerFactory.getLogger(CommentHelper.class);
    private final WebClient webClient;
    private static final String FILENAME = "{filename:.+}";

    public ProductHelper() {
        this.webClient = WebClient.create("http://localhost:9090");
    }
    @HystrixCommand(fallbackMethod = "defaultProducts")
    public Flux<Product> getAllProducts(){
        Mono<String> monoUsername = ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    User user = (User)securityContext.getAuthentication().getPrincipal();
                    return Mono.just(user.getUsername());
                })
                .defaultIfEmpty("");

        return monoUsername.flatMapMany(username->{
           if(username == ""){
               return webClient.get()
                       .uri(uriBuilder -> uriBuilder.path("/products")
                               .queryParam("username","")
                               .build())
                       .retrieve()
                       .bodyToFlux(Product.class);
           }else{
               return webClient.get()
                       .uri(uriBuilder -> uriBuilder.path("/products")
                               .queryParam("username",username)
                               .build())
                       .retrieve()
                       .bodyToFlux(Product.class);
           }
        });
    }

    public Flux<Product> defaultProducts(){
        return Flux.empty();
    }

    @HystrixCommand(fallbackMethod = "defaultProducts")
    public Flux<Product> getUserProducts(){
        Mono<String> monoUsername = ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    User user = (User)securityContext.getAuthentication().getPrincipal();
                    return Mono.just(user.getUsername());
                })
                .defaultIfEmpty("");

        return monoUsername.flatMapMany(username->{
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/products/own")
                            .queryParam("username",username)
                            .build())
                    .retrieve()
                    .bodyToFlux(Product.class);
        });
    }


    @HystrixCommand(fallbackMethod = "defaultProduct")
    public Mono<Product> getProduct(long productId){
        return webClient.get()
                .uri("/product/{productId}", productId)
                .exchange()
                .flatMap(clientResponse -> {
                    return clientResponse.bodyToMono(Product.class);
                });
    }

    public Mono<Product> defaultProduct(long productId){
        return Mono.empty();
    }

    public Mono<Resource> getProductResource(String filename){
        return webClient.get()
                .uri("/product/"+filename+"/raw")
                .exchange()
                .flatMap(clientResponse -> {
                    return clientResponse.bodyToMono(Resource.class);
                });
    }

    public Mono<Void> deleteProduct(String filename){
        return webClient.delete()
                .uri("/product/"+filename)
                .exchange()
                .flatMap(clientResponse -> {
                    return clientResponse.bodyToMono(Void.class);
                });
    }

    public Mono<Void> deleteProduct(long productId){
        return webClient.delete()
                .uri("/product/{productId}",productId)
                .exchange()
                .flatMap(clientResponse -> {
                    return clientResponse.bodyToMono(Void.class);
                });
    }
}
