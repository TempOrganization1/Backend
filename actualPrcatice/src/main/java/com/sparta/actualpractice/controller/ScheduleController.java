package com.sparta.actualpractice.controller;//package com.sparta.actualproject.controller;

import com.sparta.actualpractice.dto.request.ScheduleRequestDto;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

//    @PostMapping("{party_id}/schedules")
//    public ResponseEntity<?> createSchedules(
//            @PathVariable(name = "party_id") Long partyId,
//            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
//            @RequestBody ScheduleRequestDto scheduleRequestDto){
//
//        return scheduleService.createSchedules(partyId, memberDetails.getMember(), scheduleRequestDto);
//    }
    @PostMapping("{party_id}/schedules")
    public ResponseEntity<?> createSchedules(
        @PathVariable(name = "party_id") Long partyId,
        @AuthenticationPrincipal MemberDetailsImpl memberDetails,
        @RequestBody ScheduleRequestDto scheduleRequestDto){


    return scheduleService.createSchedules(partyId, memberDetails.getMember(), scheduleRequestDto);
}

    //개인 일정 조회 (자신이 속한 모든 그룹)
    @GetMapping("/schedules")
    public ResponseEntity<?> getSchedulesList(@AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return scheduleService.getSchedulesList(memberDetails.getMember());
    }
    // 자신이 속한 특정 그룹 일정 조회
    @GetMapping("/{party_id}/schedules")
    public ResponseEntity<?> getPartySchedulesList(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable(name = "party_id") Long partyId){

        return scheduleService.getPartySchedulesList(partyId,memberDetails.getMember());
    }

    // 본인 일정 상세 조회
    @GetMapping("/schedules/{schedule_id}")
    public ResponseEntity<?> getSchedule(@PathVariable(name = "schedule_id") Long scheduleId){

        return scheduleService.getSchedule(scheduleId);
    }

}
