package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.repositories.UserRepository;
import com.cevs.reactive.shop.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;

    public UserServiceImplementation(UserRepository userRepository, ResourceLoader resourceLoader) {
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Mono<User> getUser(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Mono<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Mono<Boolean> checkIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Mono<Resource> findOneUserImage(String imageName) {
        return Mono.fromSupplier(() ->
             resourceLoader.getResource("file:"+UPLOAD_ROOT+"/"+imageName)
        );
    }
}
