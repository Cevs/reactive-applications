package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.images.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {

    @Bean
    CommandLineRunner runner(MongoOperations operations){
        return args -> {
            operations.dropCollection(Image.class);
            operations.insert(new Image("1", "image1.jpeg"));
            operations.insert(new Image("2", "image2.jpg"));

            operations.findAll(Image.class).forEach(image->{
                System.out.println(image.getName());
            });
        };
    }
}
