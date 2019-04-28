package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.MyUserDetails;
import com.cevs.reactive.shop.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MyReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public MyReactiveUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private Logger log = LoggerFactory.getLogger(MyReactiveUserDetailsService.class);

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        System.out.println("USERNAME: "+username);
        return userRepository
                .findByUsername(username).log("FIND BY USERNAME").map(user -> {
                    log.info("USER1: "+user.toString());
                   return  user;
                })
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User not found"))))
                .map(user -> {
                    log.info("USER: "+user.toString());
                    return new MyUserDetails(user);
                });
    }
}
