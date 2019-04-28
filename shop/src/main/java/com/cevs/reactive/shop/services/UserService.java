package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.User;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> getUser(long userId);
    Mono<User> getUserByUsername(String username);
    Mono<Boolean> checkIfUserExists(String username);
    Mono<Resource> findOneUserImage(String imageName);
}
