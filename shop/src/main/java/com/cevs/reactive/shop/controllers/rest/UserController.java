package com.cevs.reactive.shop.controllers.rest;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    private static final String FILENAME = "{filename:.+}";

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/{userId}")
    public Mono<User> getUser(@PathVariable long userId){
        return userService.getUser(userId);
    }

    @GetMapping("/user/exist")
    public Mono<Boolean> checkIfExist(@RequestParam("username") String username){
        return userService.checkIfUserExists(username);
    }

    @GetMapping(value = "/user/" + FILENAME + "/raw", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> oneRawImage(@PathVariable String filename){
        return userService.findOneUserImage(filename)
                .map(resource -> {
                    try {
                        return ResponseEntity.ok().contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    }catch(IOException e){
                        return ResponseEntity.badRequest().body("Couldn't find " + filename + " => " + e.getMessage());
                    }
                });
    }
}
