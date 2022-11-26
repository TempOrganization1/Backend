package com.sparta.actualpractice.dto.response;


import com.sparta.actualpractice.entity.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
