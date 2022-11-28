package com.sparta.actualpractice.controller;

import com.sparta.actualpractice.dto.request.MemberInfoRequestDto;
import com.sparta.actualpractice.dto.request.MemberRequestDto;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto) {

        return memberService.signup(memberRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto memberRequestDto) {

        return memberService.login(memberRequestDto);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {

        return memberService.checkEmail(email);
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return memberService.getMyInfo(memberDetails.getMember());
    }

    @PatchMapping("/mypage")
    public ResponseEntity<?> updateMyInfo(@ModelAttribute MemberInfoRequestDto memberInfoRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {

        return memberService.updateMyInfo(memberInfoRequestDto, memberDetails.getMember());
    }
}
