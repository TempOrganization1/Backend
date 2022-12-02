package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Party;
import lombok.Getter;

@Getter
public class PartyCodeResponseDto {

    private String code;

    public PartyCodeResponseDto(Party party) {

        this.code = party.getCode();
    }
}
