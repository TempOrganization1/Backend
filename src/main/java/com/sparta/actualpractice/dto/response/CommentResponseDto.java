package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Comment;
import com.sparta.actualpractice.entity.Time;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String content;

    private String writer;

    private String profileImageUrl;

    private String beforeTime;


    public CommentResponseDto(Comment comment){

        this.content = comment.getContent();
        this.writer = comment.getMember().getName();
        this.profileImageUrl = comment.getMember().getImageUrl();
        this.beforeTime = Time.calculateTime(comment);
    }
}
