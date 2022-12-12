package com.sparta.actualpractice.party;


import com.sparta.actualpractice.party.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartyResponseDto {

    private Long partyId;
    private String partyName;
    private String partyIntroduction;
    private String memberEmail;

    public PartyResponseDto(Party party) {

        this.partyId = party.getId();
        this.partyName = party.getName();
        this.partyIntroduction = party.getIntroduction();

        if (party.getAdmin() != null)
            this.memberEmail = party.getAdmin().getMember().getEmail();
    }
}
