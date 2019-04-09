package com.cevs.reactivesocialappchat;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(ChatServiceStream.class)
public class InboundChatService implements WebSocketHandler {

    private final ChatServiceStream chatServiceStream;

    public InboundChatService(ChatServiceStream chatServiceStream) {
        this.chatServiceStream = chatServiceStream;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session
                .receive()
                .log("inbound-incoming-chat-message")
                .map(WebSocketMessage::getPayloadAsText)
                .log("inbound-converter-to-text")
                .map(s->session.getId() + ": " + s)
                .log("inbound-mark-with.session-id")
                .flatMap(this::broadcast)
                .log("inbound-broadcast-to-broker")
                .then();
    }

    public Mono<?> broadcast(String message){
        return Mono.fromRunnable(()->{
            chatServiceStream.clientToBroker().send(
                    MessageBuilder.withPayload(message).build()
            );
        });
    }
}
