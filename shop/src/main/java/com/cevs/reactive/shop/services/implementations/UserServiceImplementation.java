package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.repositories.UserRepository;
import com.cevs.reactive.shop.services.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> getUser(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Mono<Boolean> checkIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
