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
    private String placename;
    private String date;
    private String meetTime;

    private List<ParticipantReponseDto> participantResponseDtoList;


    public ScheduleResponseDto(String partyName, Long scheduleId, String writer) {

        this.partyName = partyName;
        this.scheduleId = scheduleId;
        this.writer = writer;
    }

    public ScheduleResponseDto(Schedule schedule, List<ParticipantReponseDto> participantResponseDtoList) {

        this.scheduleId = schedule.getId();
        this.writer = schedule.getMember().getName();
        this.profileImageUrl = schedule.getMember().getImageUrl();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.address = schedule.getPlace().split(",")[0];
        this.placename = schedule.getPlace().split(",")[1];
        this.date = schedule.getTime().split(" ")[0];
        this.meetTime = schedule.getTime().split(" ")[1];
        this.participantResponseDtoList = participantResponseDtoList;
    }


}
