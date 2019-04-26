package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.UserReview;
import com.cevs.reactive.shop.helpers.CommentHelper;
import com.cevs.reactive.shop.domain.Product;
import com.cevs.reactive.shop.helpers.ProductHelper;
import com.cevs.reactive.shop.repositories.UserRepository;
import com.cevs.reactive.shop.services.SingleProductService;
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
    UserRepository userRepository;

    private final ProductHelper productHelper;

    private final Logger log = LoggerFactory.getLogger(SingleProductServiceImpl.class);
    private final CommentHelper commentHelper;
    private final ResourceLoader resourceLoader;
    private final String UPLOAD_ROOT = "upload-dir";

    public SingleProductServiceImpl(ProductHelper productHelper, CommentHelper commentHelper, ResourceLoader resourceLoader) {
        this.productHelper = productHelper;
        this.commentHelper = commentHelper;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Flux<UserReview> getCompositeProductData(long productId) {

        return commentHelper.getReviews(productId)
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
    public Mono<Product> getProductInfo(long productId) {
        return productHelper.getProduct(productId);
    }

    @Override
    public Mono<Resource> findOneProduct(String filename){
        return Mono.fromSupplier(()->
                resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename)
        );
    }

    @Override
    public Flux<Product> getSimilarProducts(long productId) {
        return  productHelper.getAllProducts()
                .flatMap(product -> {
                    Mono<Product> monoProduct = productHelper.getProduct(productId);
                    return monoProduct.map(originalProduct -> {
                        if(product.getCategory().equals(originalProduct.getCategory()) && product.getId() != productId){
                            return product;
                        }else{
                            return new Product(0,"","",0,"","",0,false,0,"", "");
                        }
                    });
                })
                .filter(product -> !(product.getId() == 0));
    }

    @Override
    public Mono<User> getOwnerOfProduct(long productId) {
        return productHelper.getProduct(productId)
                .flatMap(product -> {
                    return userRepository.findByUsername(product.getOwner());
                });
    }
}
