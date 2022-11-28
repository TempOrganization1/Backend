package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Schedule;
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
