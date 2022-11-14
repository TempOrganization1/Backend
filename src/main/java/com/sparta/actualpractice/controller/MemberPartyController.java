package com.sparta.actualpractice.controller;

import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.MemberPartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberPartyController {

    private final MemberPartyService memberPartyService;

    @PostMapping("{party_id}/attend")
    public ResponseEntity<?> createMemberParty(@AuthenticationPrincipal MemberDetailsImpl memberDetails, @PathVariable(name = "party_id") Long partyId) {

        return memberPartyService.createMemberParty(partyId, memberDetails.getMember());
    }

}