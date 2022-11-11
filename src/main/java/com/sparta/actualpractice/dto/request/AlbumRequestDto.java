package com.sparta.actualpractice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class AlbumRequestDto {

    private String content;

    private String place;

    private MultipartFile imageUrl;
}
