package com.sparta.actualprcatice.controller;


import com.example.consolelog.dto.requestDto.MemberReqeustDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto<?> signup(@RequestBody MemberReqeustDto memberReqeustDto) {

        return memberService.signup(memberReqeustDto);
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody MemberReqeustDto memberReqeustDto, HttpServletResponse response) {

        return memberService.login(memberReqeustDto, response);
    }

    @GetMapping("/check-name")
    public ResponseDto<?> checkName(@RequestParam String name) {

        return memberService.checkName(name);
    }

    @GetMapping("/check-nickname")
    public ResponseDto<?> checkNickname(@RequestParam String nickname) {

        return memberService.checkNickname(nickname);
    }

}
