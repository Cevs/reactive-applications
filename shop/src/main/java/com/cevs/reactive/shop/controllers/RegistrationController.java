package com.cevs.reactive.shop.controllers;

import com.cevs.reactive.shop.dto.UserDto;
import com.cevs.reactive.shop.services.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Mono;

@Controller
public class RegistrationController {
    @Autowired
    UserRegistrationService userRegistrationService;

    @GetMapping("/registration")
    public Mono<String> registration(){
        return Mono.just("registration");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Mono<String> registerNewUserAccount(
            @ModelAttribute("user") UserDto userDto,
            Model model) {
        return userRegistrationService.registerNewUserAccount(userDto)
                .map(user -> redirectToLoginPage())
                .switchIfEmpty(returnRegistrationPageWithErrorMsg(model));
    }

    private String redirectToLoginPage(){
        return "redirect:/login";
    }

    private Mono<String> returnRegistrationPageWithErrorMsg(Model model){
        model.addAttribute("message", "Unsuccessful registration!");
        return Mono.just("registration");
    }
}
