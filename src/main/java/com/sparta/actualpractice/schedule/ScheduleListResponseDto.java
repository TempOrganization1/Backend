package com.sparta.actualpractice.schedule;

import com.sparta.actualpractice.schedule.Schedule;
import lombok.Getter;

@Getter
public class ScheduleListResponseDto {

    private String partyName;
    private Long scheduleId;
    private String writer;
    private String title;
    private Long partyId;

    public ScheduleListResponseDto(Schedule schedule) {

        this.partyName = schedule.getParty().getName();
        this.scheduleId = schedule.getId();
        this.writer = schedule.getMember().getName();
        this.title = schedule.getTitle();
        this.partyId = schedule.getParty().getId();
    }
}
