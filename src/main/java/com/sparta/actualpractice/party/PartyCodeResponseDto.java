package com.sparta.actualpractice.party;

import lombok.Getter;

@Getter
public class PartyCodeResponseDto {

    private String code;

    public PartyCodeResponseDto(Party party) {

        this.code = party.getCode();
    }
}
