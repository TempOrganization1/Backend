package com.sparta.actualprcatice.controller;

import com.sparta.actualprcatice.dto.request.PartyRequestDto;
import com.sparta.actualprcatice.security.MemberDetailsImpl;
import com.sparta.actualprcatice.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<?> createParty(@RequestBody PartyRequestDto partyRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return partyService.createParty(partyRequestDto,memberDetails.getMember());
    }
}
