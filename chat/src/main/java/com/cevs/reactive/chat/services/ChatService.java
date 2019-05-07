package com.cevs.reactive.chat.services;

import com.cevs.reactive.chat.domain.Chat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatService {
    Flux<Chat> getUserChats(String chatOwner, List<String> participants);
    Mono<Void> createUserChatStore(String username);
}
