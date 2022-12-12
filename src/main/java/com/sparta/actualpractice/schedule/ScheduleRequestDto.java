package com.sparta.actualpractice.schedule;

import com.sparta.actualpractice.schedule.PlaceDto;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    private String title;
    private String content;
    private PlaceDto place;
    private String meetTime;
    private String date;
}
