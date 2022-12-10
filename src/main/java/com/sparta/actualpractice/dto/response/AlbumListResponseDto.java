package com.sparta.actualpractice.dto.response;


import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class AlbumListResponseDto {

    private Long id;
    private String imageUrl;

    public AlbumListResponseDto(Album album, Image image) {

        this.id = album.getId();
        this.imageUrl = image.getImageUrl();
    }
}
