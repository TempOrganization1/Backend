package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Party;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleNullResponseDto {

    private String partyName;
    private String partyIntroduction;
    private List<MemberResponseDto> participantList;

    public ScheduleNullResponseDto(Party party, List<MemberResponseDto> memberResponseDtoList) {

        this.partyName = party.getName();
        this.partyIntroduction = party.getIntroduction();
        this.participantList = memberResponseDtoList;
    }
}
