package com.cevs.reactivesocialapp.services.implementations;

import com.cevs.reactivesocialapp.domain.User;
import com.cevs.reactivesocialapp.repositories.UserRepository;
import com.cevs.reactivesocialapp.services.UserService;
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
}
