package com.sparta.actualprcatice.dto.requestDto;

import lombok.Getter;

@Getter
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;
}
