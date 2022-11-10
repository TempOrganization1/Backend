package com.sparta.actualpractice.dto.request;

import com.sparta.actualpractice.dto.response.PlaceDto;
import lombok.Getter;
import org.joda.time.DateTime;

@Getter
public class ScheduleRequestDto {

    private String title;

    private String content;

    private PlaceDto place;

    private String meetTime;

    private String date;

}
