package com.cevs.reactive.shop.controllers;

import com.cevs.reactive.shop.dto.ProfileDto;
import com.cevs.reactive.shop.dto.UserDto;
import com.cevs.reactive.shop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public Mono<String> profilePage(Model model){
        model.addAttribute("user", userService.getUser());
        return Mono.just("profile");
    }

    @PostMapping("/profile")
    public Mono<String> updateProfile(ProfileDto profileDto){
        return userService.updateUser(profileDto).then(Mono.just("redirect:/profile"));
    }
}
