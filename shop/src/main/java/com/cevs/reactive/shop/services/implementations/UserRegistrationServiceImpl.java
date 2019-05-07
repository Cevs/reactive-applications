package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.UserDto;
import com.cevs.reactive.shop.helpers.ChatHelper;
import com.cevs.reactive.shop.repositories.UserRepository;
import com.cevs.reactive.shop.services.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final ChatHelper chatHelper;
    private final Logger log = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    public UserRegistrationServiceImpl(UserRepository userRepository, ChatHelper chatHelper) {
        this.userRepository = userRepository;
        this.chatHelper = chatHelper;
    }

    @Override
    public Mono<User> registerNewUserAccount(UserDto userDto) {
        Mono<Long> lastInsertedUser = userRepository.findTopByOrderByIdDesc().map(user -> {
            return user.getId();
        });

        Mono<User> createNewUser = userRepository.existsByUsername(userDto.getUsername())
                .flatMap(exists -> {
                    if(!exists){
                        return chatHelper.createUserChatStore(userDto.getUsername())
                                .then(Mono.just(new User(userDto.getEmail(),userDto.getUsername(),userDto.getPassword())));
                    }else{
                        return Mono.empty();
                    }
                });

        return Mono.zip(createNewUser, lastInsertedUser).flatMap(tuple2 ->{
            long newId = tuple2.getT2() + 1;
            User newUser = tuple2.getT1();
            newUser.setId(newId);
            return userRepository.save(newUser);
        });

    }
}
