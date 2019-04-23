package com.cevs.reactive.chat.services;

import com.cevs.reactive.chat.UserParsingHandshakeHandler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(ChatServiceStream.class)
public class InboundChatService extends UserParsingHandshakeHandler {

    private final ChatServiceStream chatServiceStream;

    public InboundChatService(ChatServiceStream chatServiceStream) {
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
                .flatMap(message ->
                        broadcast(message, getUser(session.getId())))
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
}
