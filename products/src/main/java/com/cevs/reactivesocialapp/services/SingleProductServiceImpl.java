package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.User;
import com.cevs.reactivesocialapp.dto.UserReview;
import com.cevs.reactivesocialapp.CommentHelper;
import com.cevs.reactivesocialapp.domain.Product;
import com.cevs.reactivesocialapp.repositories.ProductRepository;
import com.cevs.reactivesocialapp.repositories.UserRepository;
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
    public Mono<Product> getProductInfo(long productId) {
        return productRepository.findById(productId).map(product ->{
            log.info("PRODUCT_DEBUG: "+product.toString());
            return product;
        });
    }

    @Override
    public Mono<Resource> findOneProduct(String filename){
        return Mono.fromSupplier(()->
                resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename)
        );
    }

    @Override
    public Flux<Product> getSimilarProducts(long productId) {
        return productRepository.findAll()
                .flatMap(product -> {
                    Mono<Product> monoProduct = productRepository.findById(productId);
                    return monoProduct.map(originalProduct -> {
                        if(product.getCategory().equals(originalProduct.getCategory())){
                            return product;
                        }else{
                            return new Product(0,"","",0,"",0,false,0,"");
                        }
                    });
                })
                .filter(product -> !(product.getId() == 0));
    }
}
