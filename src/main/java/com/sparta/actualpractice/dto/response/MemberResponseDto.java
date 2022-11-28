package com.sparta.actualpractice.dto.response;


import com.sparta.actualpractice.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private String memberName;
    private String profileImageUrl;

    public MemberResponseDto(Member member) {

        this.memberName = member.getName();
        this.profileImageUrl = member.getImageUrl();
    }

}
