package com.sparta.actualpractice.album;

import com.sparta.actualpractice.album.Album;
import lombok.Getter;

@Getter
public class AlbumCreationResponseDto {

    private Long albumId;
    private String content;
    private String place;
    private String imageUrl;
    private String memberEmail;

    public AlbumCreationResponseDto(Album album) {

        this.albumId = album.getId();
        this.content = album.getContent();
        this.place = album.getPlace();
        this.imageUrl = album.getImageUrl();
        this.memberEmail = album.getMember().getEmail();
    }
}
