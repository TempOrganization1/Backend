package com.sparta.actualprcatice.dto.responseDto;

import com.example.consolelog.entity.Comment;
import com.example.consolelog.entity.Time;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long commentId;

    private String comment;

    private String nickname;

    private String dayBefore;


    public CommentResponseDto(Comment comment){

        this.commentId = comment.getId();
        this.comment = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.dayBefore = Time.calculateTime(comment);
    }

}
