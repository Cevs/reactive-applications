package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.products.Comment;
import com.cevs.reactivesocialapp.products.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {

    @Bean
    CommandLineRunner init(MongoOperations operations){
        return args -> {
            operations.dropCollection(Product.class);
            operations.dropCollection(Comment.class);
        };
    }
}
