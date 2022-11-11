package com.sparta.actualpractice.dto.response;


import com.sparta.actualpractice.entity.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartyResponseDto {

    private Long partyId;

    private String partyName;

    private String partyIntroduction;

    public PartyResponseDto(Party party){
        this.partyId = party.getId();
        this.partyName = party.getName();
        this.partyIntroduction = party.getIntroduction();
    }
}
