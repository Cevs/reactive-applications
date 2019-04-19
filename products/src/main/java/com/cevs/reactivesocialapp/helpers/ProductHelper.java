package com.cevs.reactivesocialapp.helpers;


import com.cevs.reactivesocialapp.domain.Product;
import com.cevs.reactivesocialapp.dto.ProductDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;
import reactor.util.function.Tuple2;


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
        return webClient.get()
                .uri("/products")
                .exchange()
                .flatMapMany(clientResponse -> {
                   return clientResponse.bodyToFlux(Product.class);
                });
    }

    public Flux<Product> defaultProducts(){
        return Flux.empty();
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

    public Mono<Void> insertProduct(ProductDto productDto){
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image", productDto.getImage());

        return webClient.post()
               .uri("/product/new")
               .body(BodyInserters.fromObject(productDto))
               .exchange()
               .flatMap(clientResponse -> {
                    return clientResponse.bodyToMono(Void.class);
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
}
