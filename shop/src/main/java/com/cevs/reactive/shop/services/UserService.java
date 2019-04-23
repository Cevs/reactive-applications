package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> getUser(long userId);
}
