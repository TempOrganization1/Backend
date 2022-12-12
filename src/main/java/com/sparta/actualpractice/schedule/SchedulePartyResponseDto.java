package com.sparta.actualpractice.schedule;

import com.sparta.actualpractice.member.MemberResponseDto;
import com.sparta.actualpractice.party.Party;
import com.sparta.actualpractice.schedule.Schedule;
import lombok.Getter;

import java.util.List;

@Getter
public class SchedulePartyResponseDto {

    private String profileImageUrl;
    private Long scheduleId;
    private String writer;
    private String title;
    private String content;
    private String address;
    private String placeName;
    private String date;
    private String meetTime;
    private String partyName;
    private String partyIntroduction;
    private Boolean isParticipant;
    private int participantSize;
    private List<MemberResponseDto> participantList;
    private Long chatRoomId;

    public SchedulePartyResponseDto(Schedule schedule, Boolean isParticipant, Party party, List<MemberResponseDto> memberResponseDtoList) {

        this.scheduleId = schedule.getId();
        this.writer = schedule.getMember().getName();
        this.profileImageUrl = schedule.getMember().getImageUrl();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.address = schedule.getPlace().split(",")[0];
        this.placeName = schedule.getPlace().split(",")[1];
        this.date = schedule.getTime().split(" ")[0];
        this.meetTime = schedule.getTime().split(" ")[1];
        this.participantSize = schedule.getParticipantList().size();
        this.isParticipant = isParticipant;
        this.partyName = party.getName();
        this.partyIntroduction = party.getIntroduction();
        this.participantList = memberResponseDtoList;
        this.chatRoomId = party.getChatRoom().getId();
    }
}
