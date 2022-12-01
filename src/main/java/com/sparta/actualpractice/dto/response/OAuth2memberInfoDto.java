package com.sparta.actualpractice.dto.response;

import lombok.Getter;

@Getter
public class OAuth2memberInfoDto {

    private Long id;
    private String nickname;
    private String email;
    private String imageUrl;

    public OAuth2memberInfoDto(Long id, String nickname, String email, String imageUrl) {

        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl =imageUrl;
    }
}
