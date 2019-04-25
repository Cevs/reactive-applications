package com.cevs.reactive.shop.controllers.rest;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/{userId}")
    public Mono<User> getUser(@PathVariable long userId){
        return userService.getUser(userId);
    }

    @GetMapping("/user/exist")
    public Mono<Boolean> checkIfExist(@RequestParam("username") String username){
        return userService.checkIfUserExists(username);
    }
}
