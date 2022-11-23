//package com.sparta.actualpractice.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.*;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//
//        registry.addEndpoint("/ws-wef")
//                .setAllowedOrigins("https://localhost:8080", "https://main.daegm2i4mn3we.amplifyapp.com")
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//
//        registry.setApplicationDestinationPrefixes("/pub");
//        registry.enableSimpleBroker("/sub");
//    }
//}
