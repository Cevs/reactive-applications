package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> getUser(long userId);
}
