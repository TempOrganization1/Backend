package com.sparta.actualpractice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class AlbumResponseDto {

    private String content;

    private String writer;

    private String place;

    private String profileImageUrl;

    //private List<CommentResponseDto> commentList;

    private String imageUrl;

    private String beforeTime;


}
