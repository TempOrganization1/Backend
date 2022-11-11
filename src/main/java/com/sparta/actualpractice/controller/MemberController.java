package com.sparta.actualpractice.controller;

import com.sparta.actualpractice.dto.request.MemberReqeustDto;
import com.sparta.actualpractice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberReqeustDto memberReqeustDto) {

        return memberService.signup(memberReqeustDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberReqeustDto memberReqeustDto) {

        return memberService.login(memberReqeustDto);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {

        return memberService.checkEmail(email);
    }


}
