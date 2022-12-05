package com.sparta.actualpractice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.actualpractice.dto.request.MemberInfoRequestDto;
import com.sparta.actualpractice.dto.request.MemberRequestDto;
import com.sparta.actualpractice.dto.request.TokenRequestDto;
import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.service.KakaoService;
import com.sparta.actualpractice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto) {

        return memberService.signup(memberRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto memberRequestDto) {

        return memberService.login(memberRequestDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto tokenRequestDto){

        return memberService.reissue(tokenRequestDto);
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

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code);
    }
}
