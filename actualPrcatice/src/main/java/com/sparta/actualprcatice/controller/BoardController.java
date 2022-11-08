package com.sparta.actualprcatice.controller;

import com.example.consolelog.dto.requestDto.BoardRequestDto;
import com.example.consolelog.dto.responseDto.BoardResponseDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.service.BoardService;
import com.example.consolelog.service.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;


    // 게시물 작성
    @PostMapping(value = "/write")
    public ResponseDto<?> writeBoard(@AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.writeBoard(memberDetails.getMember());
    }


    // 게시물 업로드
    @PutMapping(value = "/write/{board_id}")
    public ResponseDto<?> uploadBoard(@PathVariable(name = "board_id") Long boardId, @RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return boardService.uploadBoard(boardId, boardRequestDto, memberDetails.getMember());
    }


    // 게시물 작성 취소
    @DeleteMapping("/write/{board_id}")
    public ResponseDto<?> cancelBoard(@PathVariable(name = "board_id") Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){

        return boardService.cancelBoard(boardId, memberDetails.getMember());
    }


    // 게시물 전체 조회
    @GetMapping
    public ResponseDto<?> getBoardList() {

        return boardService.getBoardList();
    }


    // 무한 스크롤
    @GetMapping(value = "/infinite-scroll")
    public Map<String, List<BoardResponseDto>> getBoardListScroll(@RequestParam(required = false) Integer page,
                                                                  @RequestParam(required = false) Integer size,
                                                                  @RequestParam(required = false) String sortBy,
                                                                  @RequestParam(required = false) Boolean isAsc) {
        if (isNotNullParam(page, size, sortBy, isAsc)) {
            page -= 1;

            return boardService.getBoardListScroll(page, size, sortBy, isAsc);
        } else {
            throw new RuntimeException("페이지가 없습니다.");
        }

    }


    private boolean isNotNullParam(Integer page, Integer size, String sortBy, Boolean isAsc) {

        return (page != null) && (size != null) && (sortBy != null) && (isAsc != null);
    }
    // 무한스크롤 끝


    // 게시물 상세 조회
    @GetMapping(value = "/{board_id}")
    public ResponseDto<?> getBoard(@PathVariable(name = "board_id") Long boardId) {

        return boardService.getBoard(boardId);
    }


    // 게시물 수정
    @PutMapping(value = "/{board_id}")
    public ResponseDto<?> updateBoard(@PathVariable(name = "board_id") Long boardId, @RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return boardService.updateBoard(boardId, boardRequestDto, memberDetails.getMember());
    }


    // 게시글 삭제
    @DeleteMapping(value = "/{board_id}")
    public ResponseDto<?> deleteBoard(@PathVariable(name = "board_id") Long boardId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return boardService.deleteBoard(boardId, memberDetails.getMember());
    }


    // 이미지 업로드
    @PostMapping(value = "/{board_id}/images")
    public ResponseDto<?> uploadImage(@PathVariable(name = "board_id") Long boardId, @RequestParam("images") MultipartFile image) throws IOException {

        return boardService.uploadImage(boardId, image);
    }


    // 트렌딩 정렬
    @GetMapping(value = "/heart/{options}")
    public ResponseDto<?> getTrendingBoard(@PathVariable String options) {

        return boardService.getTrendingBoard(options);

    }
}
