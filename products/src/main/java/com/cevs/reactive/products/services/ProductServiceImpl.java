package com.cevs.reactive.products.services;

import com.cevs.reactive.products.domain.Product;
import com.cevs.reactive.products.dto.ProductDto;
import com.cevs.reactive.products.repositories.ProductRepository;
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
    public Flux<Product> findAllNotOwnedProducts(String username) {
        return productRepository.findByOwnerNotContaining(username);
    }

    @Override
    public Flux<Product> findAllProductsOwnedBy(String username) {
        return productRepository.findByOwner(username);
    }


    @Override
    public Flux<Product> findProductsBySearchNameAndCategoryAndLocationAndPriceRange(
            String productName, String category, String location, double lowerLimit, double upperLimit
    ) {
        return productRepository.findByNameContainsAndCategoryContainsAndLocationNameContainsAndPriceBetween(
                productName,category,location, lowerLimit, upperLimit
        );
    }

    @Override
    public Flux<Product> findProductsBySearchNameAndCategoryAndLocation(String productName, String category, String location) {
        return productRepository.findByNameContainsAndCategoryContainsAndLocationNameContains(
                productName,category,location
        );
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
                            product.getLocationName(),
                            product.getQuantity(),
                            product.isAvailable(),
                            product.getBaseDiscount(),
                            "image"+newId+".jpg",
                            product.getOwner()
                    );
                })
                .flatMap(newProduct -> {
                    return productRepository.save(newProduct);
                });


        return saveProductDb.zipWhen(savedProduct -> {
            ProductDto updateProductDto = product;
            updateProductDto.setId(savedProduct.getId());
            return copyFile(updateProductDto).map(aVoid -> "ok");
        }).then();
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

        Mono<Product> findProduct = productRepository.findById(productDto.getId());

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

        return findProduct.flatMap(product -> {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(productDto.getCategory());
            product.setLocationName(productDto.getLocationName());
            product.setQuantity(productDto.getQuantity());
            product.setAvailable(productDto.isAvailable());
            product.setBaseDiscount(productDto.getBaseDiscount());
            return productRepository.save(product);
        }).then(deleteFile)
                .then(copyFile(productDto)).then();

    }



    private Mono<Void> copyFile(ProductDto productDto){
        if(!productDto.getImage().filename().isEmpty()){
            String imageName = "image"+productDto.getId()+".jpg";
            return Mono.just(Paths.get(UPLOAD_ROOT+"/"+imageName).toFile())
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
