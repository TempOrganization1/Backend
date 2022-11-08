package com.sparta.actualprcatice.controller;

import com.sparta.actualprcatice.dto.request.PartyRequestDto;
import com.sparta.actualprcatice.security.MemberDetailsImpl;
import com.sparta.actualprcatice.service.PartyService;
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
    public ResponseEntity<?> createParty(@RequestBody PartyRequestDto partyRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return partyService.createParty(partyRequestDto,memberDetails.getMember());
    }

     // 그룹 전체 조회
    @GetMapping
    public ResponseEntity<?> getPartyList(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return partyService.getPartyList(memberDetails.getMember());
    }
}
