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

    private final int numberOfUsers = 10;
    private final int numberOfProducts = 20;
    private final int numberOfComments = 100;
    private final int numberOfAdvertisements = 3;

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
        for(int i = 1; i<=numberOfAdvertisements; i++){
            advertisementList.add(getAdvertisement(i));
        }
        operations.insertAll(advertisementList);
    }

    private Advertisement getAdvertisement(int id){
        String imageName = "advertisement" + id + ".jpg";
        return new Advertisement(id+"", imageName);
    }

    private void insertComments() {
        reviewList = new ArrayList<>();
        for(int i = 1; i<=numberOfComments; i++){
            reviewList.add(generateRandomReview());
        }
        operations.insertAll(reviewList);
    }

    private Review generateRandomReview(){
        return new Review(
                userList.get((int)Math.floor(Math.random()*numberOfUsers)).getId(),
                productList.get((int)Math.floor(Math.random()*numberOfProducts)).getId(),
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

        for(int i = 1; i<=numberOfUsers; i++){
            userList.add(generateRandomUser(i));
        }

        operations.insertAll(userList);
    }

    private User generateRandomUser(int id){
        String firstName = lorem.getFirstName();
        String lastName = lorem.getLastName();
        String email = lastName+"."+firstName+"@gmail.com";
        return new User(
                id+"",
                email,
                firstName+lastName,
                getRandomUserImage()
        );
    }

    private String getRandomUserImage(){
        return  "profile" + (int)Math.ceil(Math.random()*numberOfUsers) + ".jpg";
    }

    private void insertProducts(){
        productList = new ArrayList<>();

        for(int i = 1; i<=numberOfProducts; i++){
            productList.add(generateRandomProduct(i));
        }

        operations.insertAll(productList);
    }

    private Product generateRandomProduct(int id){
        return new Product(
                id+"",
                generateProductName(id),
                generateProductDescription(),
                generateProductPrice(),
                generateProductCategory(),
                generateProductImageName()
        );
    }

    private String generateProductName(int id){
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
    private String generateProductImageName(){
        return "image" + (int)Math.ceil(Math.random()*7) +".jpg";
    }
}
