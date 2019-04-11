package com.cevs.reactivesocialapp.products;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ProductService {

    private static String UPLOAD_ROOT = "upload-dir";

    //Base folder where products will be saved
    private final ResourceLoader resourceLoader;
    private final ProductRepository productRepository;

    public ProductService(ResourceLoader resourceLoader, ProductRepository productRepository) {
        this.resourceLoader = resourceLoader;
        this.productRepository = productRepository;
    }

    public Flux<Product> findAllProducts() {
        return productRepository.findAll().log("findAll");
    }

    public Mono<Resource> findOneProduct(String filename){
        //If we put Mono.just() instead of Mono.fromSupplier() the resource fetching would happen immediately
        //when the method is called.
        //By putting it inside a Java 8 Supplier, that won't happen until the lambda is invoked, and because
        //its wrapped by a Mono, invocation won't happen until the client subscribes
        return Mono.fromSupplier(()->
           resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename)
        );
    }

    public Mono<Void> createProduct(Flux<FilePart> files){
        return files
                .log("createImage-files")
                .flatMap(file ->{
                    Mono<Product> saveDatabaseImage = productRepository.save(
                            new Product(
                                    UUID.randomUUID().toString(),
                                    file.filename()

                            )
                    ).log("createImage-save");

                    Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT,file.filename()).toFile())
                            .log("createImage-picktarget")
                            .map(destFile ->{
                                try {
                                    destFile.createNewFile();
                                    return destFile;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .log("createImage-flatMap")
                            .flatMap(file::transferTo)
                            .log("createImage-copy");
                    //To ensure both of these operations are completed, join them together
                    return Mono.when(saveDatabaseImage, copyFile).log("createImage-when");
                })
                .log("createImage-flatMap")
                .then()
                .log("createImage-then"); //Signal when all files have been processed
    }

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
