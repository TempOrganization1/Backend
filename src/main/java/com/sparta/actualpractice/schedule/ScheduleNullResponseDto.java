package com.sparta.actualpractice.schedule;

import com.sparta.actualpractice.member.MemberResponseDto;
import com.sparta.actualpractice.party.Party;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleNullResponseDto {

    private String partyName;
    private String partyIntroduction;
    private Long scheduleId;
    private List<MemberResponseDto> participantList;

    public ScheduleNullResponseDto(Party party, List<MemberResponseDto> memberResponseDtoList) {

        this.partyName = party.getName();
        this.partyIntroduction = party.getIntroduction();
        this.participantList = memberResponseDtoList;
        this.scheduleId = null;
    }
}
