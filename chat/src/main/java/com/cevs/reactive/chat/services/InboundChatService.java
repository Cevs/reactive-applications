package com.cevs.reactive.chat.services;

import com.cevs.reactive.chat.UserParsingHandshakeHandler;
import com.cevs.reactive.chat.domain.Chat;
import com.cevs.reactive.chat.domain.Message;
import com.cevs.reactive.chat.domain.UserChatStore;
import com.cevs.reactive.chat.repositories.UserChatStoreRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@EnableBinding(ChatServiceStream.class)
public class InboundChatService extends UserParsingHandshakeHandler {

    private final UserChatStoreRepository userChatStoreRepository;
    private final ChatServiceStream chatServiceStream;
    private final Logger log = LoggerFactory.getLogger(InboundChatService.class);

    public InboundChatService(UserChatStoreRepository userChatStoreRepository, ChatServiceStream chatServiceStream) {
        this.userChatStoreRepository = userChatStoreRepository;
        this.chatServiceStream = chatServiceStream;
    }

    @Override
    protected Mono<Void> handleInternal(WebSocketSession session) {
        return session
                .receive()
                .log(getUser(session.getId())
                        + "-inbound-incoming-chat-message")
                .map(WebSocketMessage::getPayloadAsText)
                .log(getUser(session.getId())
                        + "-inbound-convert-to-text")
                .flatMap(message ->{
                    if(!message.isEmpty()){
                        return saveToDatabase(message).then(broadcast(message,getUser(session.getId())));
                    }
                    else{
                        return Mono.empty();
                    }
                })
                .log(getUser(session.getId())
                        + "-inbound-broadcast-to-broker")
                .then();
    }

    public Mono<?> broadcast(String message, String user){
        return Mono.fromRunnable(()->{
            chatServiceStream.clientToBroker().send(
                    MessageBuilder
                            .withPayload(message)
                            .setHeader(ChatServiceStream.USER_HEADER, user)
                            .build()
            );
        });
    }


    private Mono<Void> saveToDatabase(String message){
        JsonObject objMsg = new JsonParser().parse(message).getAsJsonObject();
        String receiver = getReceiver(objMsg);
        String sender = getSender(objMsg);
        String messageContent = getMessage(objMsg);
        Message messageObj = new Message(receiver,sender,messageContent);
        return Mono.when(updateUserStore(sender, receiver, messageObj), updateUserStore(receiver,sender,messageObj));
    }

    private Mono<UserChatStore> updateUserStore(String chatOwner, String participant, Message message){
        return userChatStoreRepository.findById(chatOwner)
                .flatMap(userChatStore -> {
                    Mono<Chat> chatMono = Flux.fromIterable(userChatStore.getChats())
                            .filter(chat -> chat.getReceiver().equals(participant))
                            .next()
                            .defaultIfEmpty(new Chat(participant));

                    return chatMono.flatMap(chat -> {
                        chat.getMessages().add(message);
                        List<Chat> existingChats = userChatStore.getChats();
                        if(!existingChats.contains(chat)){
                            existingChats.add(chat);
                            userChatStore.setChats(existingChats);
                        }
                        return userChatStoreRepository.save(userChatStore);
                    });
                });
    }

    private String getSender(JsonObject jsonMessageObject){
        return jsonMessageObject.get("sender").getAsString();
    }

    private String getReceiver(JsonObject jsonMessageObject){
        return jsonMessageObject.get("receiver").getAsString();
    }

    private String getMessage(JsonObject jsonMessageObject){
        return jsonMessageObject.get("message").getAsString();
    }
}
