package com.sparta.actualpractice.participant;

import com.sparta.actualpractice.participant.Participant;
import lombok.Getter;

@Getter
public class ParticipantReponseDto {

    private String memberName;
    private String profileImageUrl;

    public ParticipantReponseDto(Participant participant) {

        this.memberName = participant.getMember().getName();
        this.profileImageUrl = participant.getMember().getImageUrl();
    }
}
