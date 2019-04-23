package com.cevs.reactivesocialappproducts.services;

import com.cevs.reactivesocialappproducts.domain.Product;
import com.cevs.reactivesocialappproducts.dto.ProductDto;
import com.cevs.reactivesocialappproducts.repositories.ProductRepository;
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
    private final ResourceLoader resourceLoader;
    private final ProductRepository productRepository;


    public ProductServiceImpl(ResourceLoader resourceLoader, ProductRepository productRepository) {
        this.resourceLoader = resourceLoader;
        this.productRepository = productRepository;
    }

    @Override
    public Flux<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Resource> findOneProduct(String filename) {
        return Mono.fromSupplier(()->
            resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename)
        );
    }

    @Override
    public Mono<Void> insertProduct(ProductDto product) {
        Mono<Product> monoLastProduct = productRepository.findTopByOrderByIdDesc();

        Mono<Product> saveProductDb = monoLastProduct
                .map(lastProduct -> {
                    return lastProduct.getId();
                }).map(id -> {
                    long newId = id+1;
                    return new Product(
                            newId,
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getCategory(),
                            product.getQuantity(),
                            product.isAvailable(),
                            product.getBaseDiscount(),
                            "image"+newId+".jpg"
                    );
                })
                .flatMap(newProduct -> {
                    return productRepository.save(newProduct);
                });


        return Mono.when(copyFile(product), saveProductDb);
    }

    @Override
    public Mono<Void> deleteProduct(long productId) {
        Mono<Void> deleteDatabaseProduct = productRepository
                .findById(productId)
                .flatMap(product -> productRepository.delete(product));

        Mono<Void> deleteFiles = Mono.fromRunnable(()->{
            try{
                String filename = "image"+productId+".jpg";
                Files.deleteIfExists(
                        Paths.get(UPLOAD_ROOT, filename)
                );
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

        return Mono.when(deleteDatabaseProduct,deleteFiles).then();
    }

    @Override
    public Mono<Product> getProduct(long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Mono<Void> updateProduct(ProductDto productDto) {

        Mono<Void> deleteProduct = productRepository.findById(productDto.getId())
                .flatMap(product -> {
                    return productRepository.delete(product);
                });


        Mono<Void> deleteFile = Mono.fromRunnable(()->{
            try{
                if(!productDto.getImage().filename().isEmpty()){
                    String filename = "image"+productDto.getId()+".jpg";
                    Files.deleteIfExists(
                            Paths.get(UPLOAD_ROOT, filename)
                    );
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

        return deleteFile
                .then(deleteProduct)
                .then(copyFile(productDto))
                .then(Mono.just(
                        new Product(productDto)
                ))
                .flatMap(product -> {
                    return productRepository.save(product);
                })
                .then();
    }



    private Mono<Void> copyFile(ProductDto productDto){
        if(!productDto.getImage().filename().isEmpty()){
           return Mono.just(Paths.get(UPLOAD_ROOT, "image"+productDto.getId()+".jpg").toFile())
                    .log("create-image")
                    .map(destFile->{
                        try{
                            destFile.createNewFile();
                            return destFile;
                        }catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .flatMap(productDto.getImage()::transferTo);
        }else{
            return Mono.empty();
        }
    }
}