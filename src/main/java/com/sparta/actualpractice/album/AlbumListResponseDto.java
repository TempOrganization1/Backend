package com.sparta.actualpractice.album;


import com.sparta.actualpractice.album.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlbumListResponseDto {

    private Long id;
    private String imageUrl;

    public AlbumListResponseDto(Album album) {

        this.id = album.getId();
        this.imageUrl = album.getImageUrl();
    }
}
