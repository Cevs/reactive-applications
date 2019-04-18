package com.cevs.reactivesocialapp.services.implementations;

import com.cevs.reactivesocialapp.domain.Product;
import com.cevs.reactivesocialapp.dto.ProductDto;
import com.cevs.reactivesocialapp.repositories.ProductRepository;
import com.cevs.reactivesocialapp.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ProductServiceImpl implements ProductService {

    private static String UPLOAD_ROOT = "upload-dir";
    private final static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    //Base folder where products will be saved
    private final ResourceLoader resourceLoader;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ResourceLoader resourceLoader, ProductRepository productRepository) {
        this.resourceLoader = resourceLoader;
        this.productRepository = productRepository;
    }

    @Override
    public Flux<Product> findAllProducts() {
        return productRepository.findAll().log("findAll");
    }

    @Override
    public Mono<Resource> findOneProduct(String filename){
        //If we put Mono.just() instead of Mono.fromSupplier() the resource fetching would happen immediately
        //when the method is called.
        //By putting it inside a Java 8 Supplier, that won't happen until the lambda is invoked, and because
        //its wrapped by a Mono, invocation won't happen until the client subscribes
        return Mono.fromSupplier(()->
           resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename)
        );
    }

    @Override
    public Mono<Void> insertProduct(ProductDto product){


        Mono<Product> monoLastProduct = productRepository.findTopByOrderByIdDesc();

        Mono<Product> saveProductDb = monoLastProduct
                .map(lastProduct -> {
                    return lastProduct.getId();
                }).map(id -> {
                    return new Product(
                            id+1,
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getCategory(),
                            product.getQuantity(),
                            product.isAvailable(),
                            product.getBaseDiscount(),
                            product.getImage().filename()
                    );
                })
                .flatMap(newProduct -> {
                    return productRepository.save(newProduct);
                });

        Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT, product.getImage().filename()).toFile())
                .log("create-image")
                .map(destFile->{
                    try{
                        destFile.createNewFile();
                        return destFile;
                    }catch (IOException e){
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(product.getImage()::transferTo);

        return Mono.when(copyFile, saveProductDb);
    }

    @Override
    public Mono<Void> deleteProduct(String filename){
        Mono<Void> deleteDatabaseProduct = productRepository
                .findByName(filename)
                .log("deleteImage-find")
                .flatMap(image -> productRepository.delete(image))
                .log("deleteImage-record");
        
        Mono<Void> deleteFiles = Mono.fromRunnable(()->{
            try {
                Files.deleteIfExists(
                        Paths.get(UPLOAD_ROOT, filename)
                );
            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        });

        // When using .when() and .then() is also known as PROMISE PATTERN
        return Mono.when(deleteDatabaseProduct, deleteFiles)
                .log("deleteImage-when")
                .then()
                .log("deleteImage-done");
    }
}
