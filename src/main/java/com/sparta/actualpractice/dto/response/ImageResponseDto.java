package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Image;
import lombok.Getter;

@Getter
public class ImageResponseDto {

    private Long id;
    private String imageUrl;

    public ImageResponseDto(Image image) {

        this.id = image.getId();
        this.imageUrl = image.getImageUrl();
    }
}
