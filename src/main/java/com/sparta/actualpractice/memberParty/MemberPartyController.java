package com.sparta.actualpractice.memberParty;

import com.sparta.actualpractice.security.MemberDetailsImpl;
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

    @PostMapping("{party_id}/attendant")
    public ResponseEntity<?> createMemberParty(@PathVariable(name = "party_id") Long partyId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return memberPartyService.createMemberParty(partyId, memberDetails.getMember());
    }

}