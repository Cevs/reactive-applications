package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserRegistrationService {
    public Mono<User> registerNewUserAccount(UserDto userDto);
}
