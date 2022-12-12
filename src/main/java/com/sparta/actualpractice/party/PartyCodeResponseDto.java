package com.sparta.actualpractice.party;

import com.sparta.actualpractice.party.Party;
import lombok.Getter;

@Getter
public class PartyCodeResponseDto {

    private String code;

    public PartyCodeResponseDto(Party party) {

        this.code = party.getCode();
    }
}
