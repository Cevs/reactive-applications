package com.cevs.reactive.shop;

import com.cevs.reactive.shop.domain.*;
import com.thedeanda.lorem.Lorem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class InitDatabase {

    private Lorem lorem;
    private MongoOperations operations;

    private static final int NUMBER_OF_USERS = 10;
    private static final int NUMBER_OF_PRODUCTS = 20;
    private static final int NUMBER_OF_COMMENTS = 100;
    private static final int NUMBER_OF_ADVERTISEMENTS = 3;
    private static final int MIN_PRODUCT_SUPPLIES = 10;
    private static final int MAX_PRODUCT_SUPPLIES = 50;
    private static final int MIN_BASE_DISCOUNT = 0;
    private static final int MAX_BASE_DISCOUNT = 10;
    private static final int NUMBER_OF_CATEGORIES = 5;

    private List<User> userList;
    private List<Product> productList;
    private List<Review> reviewList;
    private List<Advertisement> advertisementList;
    private List<Category> categoryList;
    private List<String> countryList = Arrays.asList("SAD", "China", "UK", "Croatia", "India", "Russia","Germany");
    private List<Location> locationList;
    private List<Role> userRoles;

    @Bean
    CommandLineRunner init(MongoOperations operations){
        this.operations = operations;
        return args -> {
            operations.dropCollection(Role.class);
            operations.dropCollection(Category.class);
            operations.dropCollection(Location.class);
            operations.dropCollection(Product.class);
            operations.dropCollection(Review.class);
            operations.dropCollection(User.class);
            operations.dropCollection(Advertisement.class);
            operations.dropCollection(ShoppingCart.class);

            insertRoles();
            insertCategories();
            insertLocations();
            insertUsers();
            insertProducts();
            insertComments();
            insertAdvertisements();
        };
    }

    private void insertRoles(){
        userRoles = new ArrayList<>();
        userRoles.add(new Role(1, "ROLE_ADMIN"));
        userRoles.add(new Role(2, "ROLE_USER"));
        operations.insertAll(userRoles);
    }

    private void insertCategories(){
        categoryList = new ArrayList<>();
        for(long i = 1; i<=NUMBER_OF_CATEGORIES; i++){
            categoryList.add(createCategory(i));
        }
        operations.insertAll(categoryList);
    }

    private Category createCategory(long id){
        return new Category(id, "Category "+id);
    }

    private void insertLocations(){
        locationList = new ArrayList<>();
        for(int i = 0; i<countryList.size()-1; i++){
            locationList.add(createLocation(i));
        }
        operations.insertAll(locationList);
    }

    private Location createLocation(int id){
        return new Location(++id, countryList.get(id));
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
        operations.insert(new User(
           11,"alemartin@foi.hr","AlenMartincevic","1234","profile11.jpg",userRoles
        ));
    }

    private User generateRandomUser(long id){
        String firstName = lorem.getFirstName();
        String lastName = lorem.getLastName();
        String email = lastName+"."+firstName+"@gmail.com";
        return new User(
                id,
                email,
                firstName+lastName,
                UUID.randomUUID().toString(),
                getRandomUserImage(),
                userRoles
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
                getCategory(),
                getLocationName(id),
                generateProductQuantity(),
                setAvailability(),
                generateBaseDiscount(),
                generateProductImageName(),
                getRandomOwner(id)
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

    private String getCategory(){
        return categoryList.get((int)(Math.floor(Math.random()*NUMBER_OF_CATEGORIES))).getName();
    }

    private String getLocationName(long productId){
        int locationId = (int) productId % locationList.size();
        return locationList.get(locationId).getCountry();
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

    private String getRandomOwner(long id){
        return userList.get((int)id % NUMBER_OF_USERS).getUsername();
    }
}
