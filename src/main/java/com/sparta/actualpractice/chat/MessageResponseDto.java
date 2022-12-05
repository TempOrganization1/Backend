package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.entity.Message;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MessageResponseDto implements Serializable {

    private Long chatRoomId;
    private Long messageId;
    private String content;
    private String createdAt;
    private String memberEmail;
    private String memberName;
    private String profileImg;

    public MessageResponseDto(Message message) {

        this.chatRoomId = message.getChatRoom().getId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.memberEmail = message.getMember().getEmail();
        this.memberName = message.getMember().getName();
        this.profileImg = message.getMember().getImageUrl();
        this.messageId = message.getId();
    }
}
