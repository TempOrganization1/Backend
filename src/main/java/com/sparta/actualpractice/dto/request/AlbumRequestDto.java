package com.sparta.actualpractice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class AlbumRequestDto {

    private String content;
    private String place;
    private List<MultipartFile> imageList;
}