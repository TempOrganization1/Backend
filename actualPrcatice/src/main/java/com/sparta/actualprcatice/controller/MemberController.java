package com.sparta.actualprcatice.controller;

import com.sparta.actualproject.dto.request.MemberReqeustDto;
import com.sparta.actualproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<?> login(@RequestBody MemberReqeustDto memberReqeustDto, HttpServletResponse response) {

        return memberService.login(memberReqeustDto, response);
    }


}
