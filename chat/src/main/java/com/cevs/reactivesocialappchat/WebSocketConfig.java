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

    @Bean
    HandlerMapping webSocketMapping(CommentService commentService){
        Map<String, WebSocketHandler> urlMap = new HashMap<>();
        urlMap.put("/topic/comments.new", commentService);
        Map<String, CorsConfiguration> corsConfigurationMap =
                new HashMap<>();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfigurationMap.put(
                "/topic/comments.new", corsConfiguration);
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
