package com.cevs.reactive.chat.services;

import com.cevs.reactive.chat.UserParsingHandshakeHandler;
import com.cevs.reactive.chat.domain.Chat;
import com.cevs.reactive.chat.repositories.ChatRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(ChatServiceStream.class)
public class InboundChatService extends UserParsingHandshakeHandler {

    private final ChatRepository chatRepository;
    private final ChatServiceStream chatServiceStream;
    private final Logger log = LoggerFactory.getLogger(InboundChatService.class);

    public InboundChatService(ChatRepository chatRepository, ChatServiceStream chatServiceStream) {
        this.chatRepository = chatRepository;
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
                            return Mono.defer(() -> {
                                return saveToDatabase(message);
                            }).flatMap(chat -> {
                                return broadcast(message, getUser(session.getId()));
                            });
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


    private Mono<Chat> saveToDatabase(String message){
        JsonObject objMsg = new JsonParser().parse(message).getAsJsonObject();
        String receiver = getReceiver(objMsg);
        String sender = getSender(objMsg);
        String messageContent = getMessage(objMsg);
        Chat chat = new Chat(receiver,sender,messageContent);
        return chatRepository.save(chat);
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
