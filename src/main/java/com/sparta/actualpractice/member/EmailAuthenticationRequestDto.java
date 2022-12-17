package com.sparta.actualpractice.member;

import lombok.Getter;

@Getter
public class EmailAuthenticationRequestDto {

    private String email;
    private String code;
}
