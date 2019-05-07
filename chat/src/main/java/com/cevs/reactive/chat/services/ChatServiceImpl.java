package com.cevs.reactive.chat.services;

import com.cevs.reactive.chat.domain.Chat;
import com.cevs.reactive.chat.domain.UserChatStore;
import com.cevs.reactive.chat.repositories.UserChatStoreRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{

    private final UserChatStoreRepository userChatStoreRepository;

    public ChatServiceImpl(UserChatStoreRepository userChatStoreRepository) {
        this.userChatStoreRepository = userChatStoreRepository;
    }


    @Override
    public Flux<Chat> getUserChats(String chatOwner, List<String> participants) {
        Mono<UserChatStore> userChatStoreMono = userChatStoreRepository.findById(chatOwner);
        Flux<Chat> chatFlux = userChatStoreMono.flatMapIterable(userChatStore -> userChatStore.getChats())
                .filter(chat -> participants.contains(chat.getReceiver()));
        return chatFlux;
    }
}
