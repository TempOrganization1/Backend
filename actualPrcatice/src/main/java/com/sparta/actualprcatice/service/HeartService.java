package com.sparta.actualprcatice.service;

import com.example.consolelog.dto.responseDto.HeartResponseDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Heart;
import com.example.consolelog.entity.Member;
import com.example.consolelog.repository.BoardRepository;
import com.example.consolelog.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final BoardRepository boardRepository;

    // 하트 생성 ♡!!
    public ResponseDto<?> addHeart(Long boardId, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if (heartRepository.existsByBoardAndMember(board, member))
            throw new IllegalArgumentException("이미 좋아요를 누른 사용자입니다.");

        Heart heart = new Heart(board, member);
        heartRepository.save(heart);

        return ResponseDto.success(new HeartResponseDto(board,"좋아요를 눌렀습니다."));
    }


    // 하트 취소 ♡ !!
    public ResponseDto<?> cancelHeart(Long board_id, Member member) {

        Board board = boardRepository.findById(board_id).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        if (!heartRepository.existsByBoardAndMember(board, member))
            throw new IllegalArgumentException("좋아요를 누른적 없는 사용자입니다.");

        Heart heart = heartRepository.findByBoardAndMember(board, member);
        heartRepository.delete(heart);

        return ResponseDto.success(new HeartResponseDto(board, "좋아요를 취소하였습니다."));
    }
}
