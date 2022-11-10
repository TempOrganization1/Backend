package com.sparta.actualpractice.dto.response;

import com.sparta.actualpractice.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleListResponseDto {

    private String partyName;
    private Long scheduleId;
    private String writer;

    public ScheduleListResponseDto(Schedule schedule) {

        this.partyName = schedule.getParty().getName();
        this.scheduleId = schedule.getId();
        this.writer = schedule.getMember().getName();
    }
}
