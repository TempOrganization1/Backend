package com.sparta.actualpractice.dto.request;

import com.sparta.actualpractice.dto.PlaceDto;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    private String title;
    private String content;
    private PlaceDto place;
    private String meetTime;
    private String date;
}
