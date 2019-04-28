package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.dto.ProfileDto;
import com.cevs.reactive.shop.dto.UserDto;
import com.cevs.reactive.shop.repositories.UserRepository;
import com.cevs.reactive.shop.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private static String UPLOAD_ROOT = "upload-dir";
    private final ResourceLoader resourceLoader;
    private Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);

    public UserServiceImplementation(UserRepository userRepository, ResourceLoader resourceLoader) {
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Mono<User> getUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context ->{
                    User user = (User) context.getAuthentication().getPrincipal();
                    return user;
                });
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
    public Mono<Void> updateUser(ProfileDto  profileDto) {

        Mono<User> monoOldUser = ReactiveSecurityContextHolder.getContext()
                .map(context ->{
                    User user = (User) context.getAuthentication().getPrincipal();
                    return user;
                });

        Mono<Void> saveImage = monoOldUser.flatMap(user -> {
           return copyFile(profileDto,user.getId());
        });

        Mono<User> updateUser = monoOldUser.flatMap(user -> {
           User updatedUser = user;
           updatedUser.setUsername(profileDto.getUsername());
           updatedUser.setEmail(profileDto.getEmail());
           updatedUser.setPassword(profileDto.getPassword());
           return userRepository.save(updatedUser);
        });


        return Mono.when(saveImage,updateUser);
    }

    private Mono<Void> copyFile(ProfileDto profileDto, long userId){
        log.info("COPY FILE");
        if(profileDto.getProfileImage() != null){
            log.info("PROFILE DTO: "+profileDto.getProfileImage().filename());
            String imageName = "profile"+userId+".jpg";
            try {
                Files.deleteIfExists(
                        Paths.get(UPLOAD_ROOT, imageName)
                );
                return Mono.just(Paths.get(UPLOAD_ROOT+"/"+imageName).toFile())
                        .log("create-image")
                        .map(destFile->{
                            try{
                                destFile.createNewFile();
                                return destFile;
                            }catch (IOException e){
                                throw new RuntimeException(e);
                            }
                        })
                        .flatMap(profileDto.getProfileImage()::transferTo);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else{
            return Mono.empty();
        }

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
