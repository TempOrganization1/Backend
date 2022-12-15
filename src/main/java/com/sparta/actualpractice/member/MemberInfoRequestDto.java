package com.sparta.actualpractice.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class MemberInfoRequestDto {

    private String memberName;
    private MultipartFile profileImage;
}
