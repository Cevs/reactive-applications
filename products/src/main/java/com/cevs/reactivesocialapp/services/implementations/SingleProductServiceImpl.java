package com.cevs.reactivesocialapp.services.implementations;

import com.cevs.reactivesocialapp.domain.User;
import com.cevs.reactivesocialapp.dto.ProductInfoDto;
import com.cevs.reactivesocialapp.dto.UserReview;
import com.cevs.reactivesocialapp.CommentHelper;
import com.cevs.reactivesocialapp.domain.Product;
import com.cevs.reactivesocialapp.repositories.CategoryRepository;
import com.cevs.reactivesocialapp.repositories.ProductRepository;
import com.cevs.reactivesocialapp.repositories.UserRepository;
import com.cevs.reactivesocialapp.services.SingleProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SingleProductServiceImpl implements SingleProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    private final Logger log = LoggerFactory.getLogger(SingleProductServiceImpl.class);
    private final CommentHelper commentHelper;
    private final ResourceLoader resourceLoader;
    private final String UPLOAD_ROOT = "upload-dir";

    public SingleProductServiceImpl(CommentHelper commentHelper, ResourceLoader resourceLoader) {
        this.commentHelper = commentHelper;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Flux<UserReview> getCompositeProductData(long productId) {

        return Flux.fromIterable(commentHelper.getComments(productId)).log("from-iterable")
                .flatMap(review -> {
                    log.info("REVIEW: " +review.toString());
                    Mono<User> userMono  = userRepository.findById(review.getUserId());
                    return userMono.map(user -> {
                        log.info("USER: "+user.toString());
                       return new UserReview(user,review);
                    });
                });
    }

    @Override
    public Mono<ProductInfoDto> getProductInfo(long productId) {
        log.info("GET PRODUCT INFO");
        return productRepository.findById(productId).flatMap(product ->{
            log.info("SEARCH PRODUCT REPOSITORY");
             return categoryRepository.findById(product.getCategoryId())
                     .map(category -> {
                         log.info("SEARCH CATEGORY REPOSITORY");
                         ProductInfoDto pid = new ProductInfoDto(
                                product.getId(),
                                product.getName(),
                                product.getDescription(),
                                product.getPrice(),
                                category.getName(),
                                product.getQuantity(),
                                product.isAvailable(),
                                product.getBaseDiscount(),
                                product.getImageName());
                         log.info("PRODUCT_INFO: "+pid.toString());
                        return pid;
                    });
        });
    }

    @Override
    public Mono<Resource> findOneProduct(String filename){
        return Mono.fromSupplier(()->
                resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename)
        );
    }

    @Override
    public Flux<ProductInfoDto> getSimilarProducts(long productId) {
        /*return productRepository.findAll()
                .flatMap(product -> {
                    Mono<Product> monoProduct = productRepository.findById(productId);
                    return monoProduct.map(originalProduct -> {
                        if(product.getCategoryId() == originalProduct.getCategoryId()){
                            return product;
                        }else{
                            return new Product(0,"","",0,0,0,false,0,"");
                        }
                    });
                })
                .filter(product -> !(product.getId() == 0));*/

        return productRepository.findAll()
                .flatMap(product -> {
                    Mono<Product> monoProduct = productRepository.findById(productId);
                    return categoryRepository.findById(product.getCategoryId())
                        .flatMap(category -> {
                              return monoProduct.map(originalProduct ->{
                                 if(product.getCategoryId() == originalProduct.getCategoryId()){
                                     return new ProductInfoDto(
                                             product.getId(),
                                             product.getName(),
                                             product.getDescription(),
                                             product.getPrice(),
                                             category.getName(),
                                             product.getQuantity(),
                                             product.isAvailable(),
                                             product.getBaseDiscount(),
                                             product.getImageName()
                                     );
                                 }else{
                                     return new ProductInfoDto(0,"","",0,"",0,false,0,"");
                                 }
                              });
                        });
                });
    }
}
