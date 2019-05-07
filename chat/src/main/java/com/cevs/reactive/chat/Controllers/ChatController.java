package com.cevs.reactive.chat.Controllers;

import com.cevs.reactive.chat.domain.Chat;
import com.cevs.reactive.chat.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class ChatController {


    @Autowired
    ChatService chatService;

    @CrossOrigin
    @PostMapping(path="/chats/{username}")
    public Flux<Chat> getUserChats(@PathVariable("username")String username,
                                   @RequestBody List<String> participants){
        return chatService.getUserChats(username,participants);
    }

    @PostMapping("/chat/store")
    public Mono<Void> createUserChatStore(@RequestBody String username){
        return chatService.createUserChatStore(username);
    }

}
