package com.sparta.actualpractice.member;

import lombok.Getter;

@Getter
public class OAuth2memberInfoDto {

    private String id;
    private String nickname;
    private String email;
    private String imageUrl;

    public OAuth2memberInfoDto(String id, String nickname, String email, String imageUrl) {

        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.imageUrl =imageUrl;
    }
}
