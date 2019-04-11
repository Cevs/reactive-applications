package com.cevs.reactivesocialappchat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    private final InboundChatService inboundChatService;
    private final OutboundChatService outboundChatService;

    public WebSocketConfig(InboundChatService inboundChatService, OutboundChatService outboundChatService) {
        this.inboundChatService = inboundChatService;
        this.outboundChatService = outboundChatService;
    }

    @Bean
    HandlerMapping webSocketMapping(CommentService commentService){
        Map<String, WebSocketHandler> urlMap = new HashMap<>();
        urlMap.put("/topic/comments.new", commentService);
        urlMap.put("/topic/chatMessage.new", outboundChatService);
        urlMap.put("/app/chatMessage.new", inboundChatService);

        Map<String, CorsConfiguration> corsConfigurationMap =
                new HashMap<>();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfigurationMap.put(
                "/topic/comments.new", corsConfiguration);
        corsConfigurationMap.put(
                "/topic/chatMessage.new", corsConfiguration
        );
        corsConfigurationMap.put(
                "/app/chatMessage.new", corsConfiguration
        );

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(10);   //Need an order level of 10 to get viewed ahead of certain other route handlers provided by spring boot
        mapping.setUrlMap(urlMap);
        mapping.setCorsConfigurations(corsConfigurationMap);
        return mapping;
    }

    /*
        Connects Spring's DispatcherHandler to a WebSocketHandler ( allows URIs to be mapped onto handler methods)
     */
    @Bean
    WebSocketHandlerAdapter handlerAdapter(){
        return new WebSocketHandlerAdapter();
    }
}
