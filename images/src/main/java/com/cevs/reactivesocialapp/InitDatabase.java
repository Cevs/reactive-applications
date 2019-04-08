package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.images.Comment;
import com.cevs.reactivesocialapp.images.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {

    @Bean
    CommandLineRunner init(MongoOperations operations){
        return args -> {
            operations.dropCollection(Image.class);
            operations.dropCollection(Comment.class);
        };
    }
}
