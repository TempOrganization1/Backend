package com.sparta.actualprcatice.controller;

import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.service.HeartService;
import com.example.consolelog.service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/{board_id}")
    public ResponseDto<?> addHeart(@PathVariable(name = "board_id") Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return heartService.addHeart(boardId, memberDetails.getMember());
    }

    @DeleteMapping("/{board_id}")
    public ResponseDto<?> cancelHeart(@PathVariable(name = "board_id") Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return heartService.cancelHeart(boardId, memberDetails.getMember());
    }

}
