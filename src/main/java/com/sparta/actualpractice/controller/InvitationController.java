package com.sparta.actualpractice.controller;


import com.sparta.actualpractice.dto.request.InvitationRequestDto;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;


    // 초대 코드 생성
    @PostMapping("/{party_id}/invitation")
    public ResponseEntity<?> createCode(@PathVariable(name = "party_id") Long partyId, @RequestBody InvitationRequestDto invitationRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return invitationService.createCode(partyId, invitationRequestDto, memberDetails.getMember());
    }
}
