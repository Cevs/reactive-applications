package com.cevs.reactive.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract  class UserParsingHandshakeHandler implements WebSocketHandler {

    private final Map<String, String> userMap;
    private Logger log = LoggerFactory.getLogger(UserParsingHandshakeHandler.class);

    public UserParsingHandshakeHandler() {
        this.userMap = new HashMap<>();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
       this.userMap.put(session.getId(),
               Stream.of(session.getHandshakeInfo().getUri()
               .getQuery().split("&"))
       .map(s->s.split("="))
       .filter(strings->strings[0].equals("user"))
       .findFirst()
       .map(strings -> {
           return strings[1];
       })
       .orElse(""));

       return handleInternal(session);
    }

    abstract protected Mono<Void> handleInternal(WebSocketSession session);

    protected String getUser(String id){
        return userMap.get(id);
    }
}
