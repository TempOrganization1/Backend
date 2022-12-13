package com.sparta.actualpractice.album;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class AlbumRequestDto {

    private String content;
    private String place;
    private MultipartFile imageUrl;
}
