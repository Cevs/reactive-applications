package com.cevs.reactive.chat.Controllers;

import com.cevs.reactive.chat.domain.Chat;
import com.cevs.reactive.chat.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {


    @Autowired
    ChatService chatService;

    @CrossOrigin
    @PostMapping(path="/chats/{username}")
    public Flux<Chat> getUserChats(@PathVariable("username")String username,
                                   @RequestBody List<String> participants){
        System.out.println("Username: "+username);
        System.out.println("Participants: "+participants);
        return chatService.getUserChats(username,participants);
    }

}
