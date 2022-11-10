package com.sparta.actualpractice.controller;

import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.PartycipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final PartycipantService partycipantService;

    @PostMapping("/{schedule_id}/participate")
    public ResponseEntity<?> createParticipant(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable(name = "schedule_id") Long scheduleId){

        return partycipantService.createParticipant(scheduleId, memberDetails.getMember().getId());
    }
}
