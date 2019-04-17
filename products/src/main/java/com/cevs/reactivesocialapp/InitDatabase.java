package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.domain.Advertisement;
import com.cevs.reactivesocialapp.domain.Review;
import com.cevs.reactivesocialapp.domain.Product;
import com.cevs.reactivesocialapp.domain.User;
import com.thedeanda.lorem.Lorem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitDatabase {

    private Lorem lorem;
    private static final Logger log = LoggerFactory.getLogger(InitDatabase.class);
    private MongoOperations operations;

    private static final int NUMBER_OF_USERS = 10;
    private static final int NUMBER_OF_PRODUCTS = 20;
    private static final int NUMBER_OF_COMMENTS = 100;
    private static final int NUMBER_OF_ADVERTISEMENTS = 3;
    private static final int MIN_PRODUCT_SUPPLIES = 10;
    private static final int MAX_PRODUCT_SUPPLIES = 50;
    private static final int MIN_BASE_DISCOUNT = 0;
    private static final int MAX_BASE_DISCOUNT = 10;

    private List<User> userList;
    private List<Product> productList;
    private List<Review> reviewList;
    private List<Advertisement> advertisementList;

    @Bean
    CommandLineRunner init(MongoOperations operations){
        this.operations = operations;
        return args -> {
            operations.dropCollection(Product.class);
            operations.dropCollection(Review.class);
            operations.dropCollection(User.class);
            operations.dropCollection(Advertisement.class);

            insertProducts();
            insertUsers();
            insertComments();
            insertAdvertisements();
        };
    }

    private void insertAdvertisements(){
        advertisementList = new ArrayList<>();
        for(long i = 1; i<=NUMBER_OF_ADVERTISEMENTS; i++){
            advertisementList.add(getAdvertisement(i));
        }
        operations.insertAll(advertisementList);
    }

    private Advertisement getAdvertisement(long id){
        String imageName = "advertisement" + id + ".jpg";
        return new Advertisement(id, imageName);
    }

    private void insertComments() {
        reviewList = new ArrayList<>();
        for(int i = 1; i<=NUMBER_OF_COMMENTS; i++){
            reviewList.add(generateRandomReview());
        }
        operations.insertAll(reviewList);
    }

    private Review generateRandomReview(){
        return new Review(
                userList.get((int)Math.floor(Math.random()*NUMBER_OF_USERS)).getId(),
                productList.get((int)Math.floor(Math.random()*NUMBER_OF_PRODUCTS)).getId(),
                lorem.getWords(10,20),
                createRandomDate(2016,2018).toString()
        );
    }


    private LocalDate createRandomDate(int startYear, int endYear){
        int day = createRandomIntBetween(1,28);
        int month = createRandomIntBetween(1,12);
        int year = createRandomIntBetween(startYear, endYear);
        return LocalDate.of(year, month, day);
    }

    private int createRandomIntBetween(int start, int end){
        return start + (int) Math.round(Math.random()*(end-start));
    }

    private void insertUsers(){
        userList = new ArrayList<>();

        for(long i = 1; i<=NUMBER_OF_USERS; i++){
            userList.add(generateRandomUser(i));
        }

        operations.insertAll(userList);
    }

    private User generateRandomUser(long id){
        String firstName = lorem.getFirstName();
        String lastName = lorem.getLastName();
        String email = lastName+"."+firstName+"@gmail.com";
        return new User(
                id,
                email,
                firstName+lastName,
                getRandomUserImage()
        );
    }

    private String getRandomUserImage(){
        return  "profile" + (int)Math.ceil(Math.random()*NUMBER_OF_USERS) + ".jpg";
    }

    private void insertProducts(){
        productList = new ArrayList<>();

        for(long i = 1; i<=NUMBER_OF_PRODUCTS; i++){
            productList.add(generateRandomProduct(i));
        }

        operations.insertAll(productList);
    }

    private Product generateRandomProduct(long id){
        return new Product(
                id,
                generateProductName(id),
                generateProductDescription(),
                generateProductPrice(),
                generateProductCategory(),
                generateProductQuantity(),
                setAvailability(),
                generateBaseDiscount(),
                generateProductImageName()
        );
    }

    private String generateProductName(long id){
        return "Product " + id;
    }

    private String generateProductDescription(){
        return lorem.getWords(10,20);
    }

    private double generateProductPrice(){
        return Math.round((3500 + Math.random()*((Math.random()*10000))));
    }

    private String generateProductCategory(){
        return "Category " + (int)Math.ceil(Math.random()*10);
    }

    private int generateProductQuantity(){
        return (int)(MIN_PRODUCT_SUPPLIES + Math.round(Math.random()*MAX_PRODUCT_SUPPLIES));
    }

    private boolean setAvailability(){
        return true;
    }

    private int generateBaseDiscount(){
        return (int)(MIN_BASE_DISCOUNT + +Math.round(Math.random()*MAX_BASE_DISCOUNT));
    }

    private String generateProductImageName(){
        return "image" + (int)Math.ceil(Math.random()*7) +".jpg";
    }
}
