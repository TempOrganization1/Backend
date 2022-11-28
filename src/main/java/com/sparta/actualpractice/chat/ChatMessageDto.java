package com.sparta.actualpractice.chat;

import lombok.Getter;

@Getter
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;
}
