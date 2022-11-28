package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
}
