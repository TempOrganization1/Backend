package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Schedule;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleResponseDto {
    private String partyName;
    private String profileImageUrl;
    private Long scheduleId;
    private String writer;
    private String title;
    private String content;
    private String address;
    private String placeName;
    private String date;
    private String meetTime;
    private List<ParticipantReponseDto> participantResponseDtoList;
    private Boolean isParticipant;
    private String memberEmail;

    public ScheduleResponseDto(Schedule schedule, List<ParticipantReponseDto> participantResponseDtoList, Boolean isParticipant) {

        this.scheduleId = schedule.getId();
        this.writer = schedule.getMember().getName();
        this.profileImageUrl = schedule.getMember().getImageUrl();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.address = schedule.getPlace().split(",")[0];
        this.placeName = schedule.getPlace().split(",")[1];
        this.date = schedule.getTime().split(" ")[0];
        this.meetTime = schedule.getTime().split(" ")[1];
        this.participantResponseDtoList = participantResponseDtoList;
        this.isParticipant = isParticipant;
        this.memberEmail = schedule.getMember().getEmail();
    }

    public ScheduleResponseDto(Schedule schedule) {

        this.writer = schedule.getMember().getName();
        this.scheduleId = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.address = schedule.getPlace().split(",")[0];
        this.placeName = schedule.getPlace().split(",")[1];
        this.date = schedule.getTime().split(" ")[0];
        this.meetTime = schedule.getTime().split(" ")[1];
        this.memberEmail = schedule.getMember().getEmail();
    }

}
