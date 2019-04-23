package com.cevs.reactive.chat.services;

import com.cevs.reactive.chat.domain.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(Sink.class)
public class CommentService implements WebSocketHandler {

    private ObjectMapper mapper;
    private Flux<Review> flux;
    private FluxSink<Review> webSocketCommentSink;
    private final static Logger log = LoggerFactory.getLogger(CommentService.class);

    public CommentService(ObjectMapper mapper) {
        this.mapper = mapper;
        this.flux = Flux.<Review>create(
                emitter -> this.webSocketCommentSink = emitter,
                FluxSink.OverflowStrategy.IGNORE)
                .publish()
                .autoConnect();
    }

    @StreamListener(Sink.INPUT)
    public void broadcast(Review review){
        log.info("STIGAO REVIEW: " + review.toString());
        if(webSocketCommentSink != null){
            log.info("Publishing " + review.toString() + " to websocket...");
            webSocketCommentSink.next(review);
        }
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(this.flux
        .map(review ->{
            try{
                return mapper.writeValueAsString(review);
            }catch (JsonProcessingException e){
                throw new RuntimeException(e);
            }
        }).log("encode-as-json")
                .map(session::textMessage)
                .log("wrap-as-websocket-message"))
                .log("publish-to-websocket");

    }


}
