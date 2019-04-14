package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.products.Comment;
import com.cevs.reactivesocialapp.products.Product;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Component
public class InitDatabase {

    private int count;
    private Lorem lorem;
    private static final Logger log = LoggerFactory.getLogger(InitDatabase.class);

    @Bean
    CommandLineRunner init(MongoOperations operations){
        return args -> {
            count = 1;
            lorem = LoremIpsum.getInstance();
            operations.dropCollection(Product.class);
            operations.dropCollection(Comment.class);

            List<Product> productList = new ArrayList<>();

            for(int i = 0; i<10000; i++){
                productList.add(generateRandomProduct());
            }

            operations.insertAll(productList);
        };
    }

    private Product generateRandomProduct(){
        return new Product(
                UUID.randomUUID().toString(),
                generateProductName(),
                generateProductDescription(),
                generateProductPrice(),
                generateProductCategory(),
                generateProductImageName()
        );
    }

    private String generateProductName(){
        return "Product " + count++;
    }

    private String generateProductDescription(){
        return lorem.getWords(10,20);
    }

    private double generateProductPrice(){
        return Math.round((3500 + Math.random()*((Math.random()*10000))));
    }

    private String generateProductCategory(){
        return "Category " + Math.ceil(Math.random()*10);
    }
    private String generateProductImageName(){
        return "image" + (int)Math.ceil(Math.random()*7) +".jpg";
    }

}
