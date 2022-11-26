package com.sparta.actualpractice.controller;

import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/{schedule_id}/participation")
    public ResponseEntity<?> createParticipant(@PathVariable(name = "schedule_id") Long scheduleId, @AuthenticationPrincipal MemberDetailsImpl memberDetails ){

        return participantService.createParticipant(scheduleId, memberDetails.getMember().getId());
    }
}
