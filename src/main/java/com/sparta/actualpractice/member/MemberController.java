package com.sparta.actualpractice.member;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.sparta.actualpractice.security.MemberDetailsImpl;
import com.sparta.actualpractice.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {

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

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequestDto emailRequestDto) throws Exception {

        return emailService.sendSimpleMessage(emailRequestDto.getEmail());
    }

    @GetMapping("/authenticate-email")
    public ResponseEntity<?> authenticateEmail(@RequestParam("email") String email, @RequestParam("code") String code) {

        return emailService.authenticateEmail(email, code);
    }

    @DeleteMapping("withdrawal")
    public ResponseEntity<?> delete(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return memberService.delete(memberDetails.getMember());
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

    @GetMapping( "/google/callback")
    public ResponseEntity<?> googleLogin(@RequestParam String code) throws JsonProcessingException {

        return googleService.googleLogin(code);
    }
}
