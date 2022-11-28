//package com.sparta.actualpractice.chat;
//
//import com.sparta.actualpractice.security.TokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
//@Component
//@RequiredArgsConstructor
//public class WebSocketHandler implements ChannelInterceptor {
//
//    private final TokenProvider tokenProvider;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
//
//            String token = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
//
//            if (!tokenProvider.validateToken(token))
//                throw new IllegalArgumentException("멤버를 찾을 수 없습니다.");
//        }
//        return message;
//    }
//}
