package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleListResponseDto {

    private String partyName;
    private Long scheduleId;
    private String writer;

    private String title;

    public ScheduleListResponseDto(Schedule schedule, String partyName) {

        this.partyName = partyName;
        this.scheduleId = schedule.getId();
        this.writer = schedule.getMember().getName();
        this.title = schedule.getTitle();
    }
    public ScheduleListResponseDto(Schedule schedule) {

        this.scheduleId = schedule.getId();
        this.writer = schedule.getMember().getName();
        this.title = schedule.getTitle();
    }



}
