package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.ProfileDto;
import com.cevs.reactive.shop.dto.UserDto;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> getUser();
    Mono<User> getUser(long userId);
    Mono<User> getUserByUsername(String username);
    Mono<Void> updateUser(ProfileDto profileDto);
    Mono<Boolean> checkIfUserExists(String username);
    Mono<Resource> findOneUserImage(String imageName);
}
