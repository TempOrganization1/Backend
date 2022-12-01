package com.sparta.actualpractice.controller;

import com.sparta.actualpractice.dto.request.PartyRequestDto;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    // 그룹 생성
    @PostMapping
    public ResponseEntity<?> createParty(@RequestBody PartyRequestDto partyRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return partyService.createParty(partyRequestDto, memberDetails.getMember());
    }

     // 그룹 전체 조회
    @GetMapping
    public ResponseEntity<?> getPartyList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return partyService.getPartyList(memberDetails.getMember());
    }

    // 그룹 메인페이지
    @GetMapping("/{party_id}/partyPage")
    public ResponseEntity<?> getPartyInfo(@PathVariable(name = "party_id") Long partyId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return partyService.getPartyInfo(partyId, memberDetails.getMember());
    }

    // 그룹 정보 수정
    @PutMapping("/{party_id}")
    public ResponseEntity<?> updateParty(@PathVariable(name = "party_id") Long partyId, @RequestBody PartyRequestDto partyRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return partyService.updateParty(partyId, partyRequestDto, memberDetails.getMember());
    }

    // 그룹 삭제
    @DeleteMapping("/{party_id}")
    public ResponseEntity<?> deleteParty(@PathVariable(name = "party_id") Long partyId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return partyService.deleteParty(partyId, memberDetails.getMember());
    }
}
