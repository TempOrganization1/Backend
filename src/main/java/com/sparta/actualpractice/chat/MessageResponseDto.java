package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.entity.Message;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MessageResponseDto implements Serializable {

    private Long chatRoomId;
    private String content;
    private String createdAt;
    private Long memberId;
    private String memberName;
    private String profileImg;

    public MessageResponseDto(Message message) {

        this.chatRoomId = message.getChatRoom().getId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.memberId = message.getMember().getId();
        this.memberName = message.getMember().getName();
        this.profileImg = message.getMember().getImageUrl();
    }
}
