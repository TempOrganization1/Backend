package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        System.out.println("message = " + message);
        System.out.println("message.getHeaders() = " + message.getHeaders());
        System.out.println("accessor.getNativeHeader(\"Authorization\") = " + accessor.getNativeHeader("Authorization"));

        if (StompCommand.CONNECT.equals(accessor.getCommand()))
            tokenProvider.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7));

        return message;
    }
}
