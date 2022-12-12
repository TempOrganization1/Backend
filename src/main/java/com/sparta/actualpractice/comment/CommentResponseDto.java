package com.sparta.actualpractice.comment;


import com.sparta.actualpractice.comment.Comment;
import com.sparta.actualpractice.comment.Time;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private String writer;
    private String profileImageUrl;
    private String beforeTime;
    private String memberEmail;

    public CommentResponseDto(Comment comment) {

        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getMember().getName();
        this.profileImageUrl = comment.getMember().getImageUrl();
        this.beforeTime = Time.calculateTime(comment);
        this.memberEmail = comment.getMember().getEmail();
    }
}