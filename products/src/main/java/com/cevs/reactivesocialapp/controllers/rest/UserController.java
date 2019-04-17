package com.cevs.reactivesocialapp.controllers.rest;

import com.cevs.reactivesocialapp.domain.User;
import com.cevs.reactivesocialapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/{userId}")
    public Mono<User> getUser(@PathVariable long userId){
        return userService.getUser(userId);
    }
}
