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

    @PostMapping("{party_id}/schedules")
    public ResponseEntity<?> createSchedule(
        @PathVariable(name = "party_id") Long partyId, @RequestBody ScheduleRequestDto scheduleRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

    return scheduleService.createSchedules(partyId, memberDetails.getMember(), scheduleRequestDto);
}

    //개인 일정 조회 (자신이 속한 모든 그룹)
    @GetMapping("/schedules")
    public ResponseEntity<?> getScheduleList(@AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return scheduleService.getScheduleList(memberDetails.getMember());
    }
    // 자신이 속한 특정 그룹 일정 조회
    @GetMapping("/{party_id}/schedules")
    public ResponseEntity<?> getPartyScheduleList(@PathVariable(name = "party_id") Long partyId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return scheduleService.getPartyScheduleList(partyId, memberDetails.getMember());
    }

    // 본인 일정 상세 조회
    @GetMapping("/schedules/{schedule_id}")
    public ResponseEntity<?> getSchedule(@PathVariable(name = "schedule_id") Long scheduleId){

        return scheduleService.getSchedule(scheduleId);
    }

    // 일정 수정
    @PutMapping("/schedules/{schedule_id}")
    public ResponseEntity<?> updateSchedule(@PathVariable(name = "schedule_id") Long scheduleId , @RequestBody ScheduleRequestDto scheduleRequestDto ,@AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return scheduleService.updateSchedule(scheduleId, scheduleRequestDto, memberDetails.getMember());
    }

}
