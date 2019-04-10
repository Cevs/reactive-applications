package com.cevs.reactivesocialappchat;

import com.cevs.reactivesocialappchat.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class InitUser {

    @Bean
    CommandLineRunner initUsers(MongoOperations operations){
        return args -> {
            operations.dropCollection(User.class);
            operations.dropCollection("sessions");
            operations.insert(
                    new User(null, "alen", "1234", new String[]{"ROLE_USER", "ROLE_ADMIN"}
            ));

            operations.insert(
                    new User(null, "taja", "4321", new String[]{"ROLE_USER"}
                    ));

            operations.findAll(User.class).forEach(user -> {
                System.out.println("Loaded: " + user);
            });
        };
    }
}
