package com.sparta.actualpractice.dto.response;


import com.sparta.actualpractice.entity.Invitation;
import lombok.Getter;

@Getter
public class InvitationResponseDto {

    private String code;


    public InvitationResponseDto(Invitation invitation) {

        this.code = invitation.getCode();
    }
}
