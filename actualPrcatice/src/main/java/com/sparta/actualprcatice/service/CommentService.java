package com.sparta.actualprcatice.service;

import com.example.consolelog.dto.requestDto.CommentRequestDto;
import com.example.consolelog.dto.responseDto.CommentResponseDto;
import com.example.consolelog.dto.responseDto.ResponseDto;
import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Comment;
import com.example.consolelog.entity.Member;
import com.example.consolelog.repository.BoardRepository;
import com.example.consolelog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 댓글 생성
    public ResponseDto<?> createComment(Long boardId, CommentRequestDto commentRequestDto, Member member) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시물이 존재하지 않습니다."));

        Comment comment = new Comment(commentRequestDto, member, board);

        commentRepository.save(comment);

        return ResponseDto.success(new CommentResponseDto(comment));
    }

    // 댓글 수정
    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, Member member) {


        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        if (validateMember(member, comment))
            throw new IllegalArgumentException("댓글 작성자와 현재 사용자가 일치하지 않습니다.");

        comment.update(commentRequestDto);

        return ResponseDto.success(new CommentResponseDto(comment));
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, Member member) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        if (validateMember(member, comment))
            throw new IllegalArgumentException("댓글 작성자와 현재 사용자가 일치하지 않습니다.");

        commentRepository.delete(comment);

        return ResponseDto.success("댓글 삭제 완료");
    }

    public boolean validateMember(Member member, Comment comment) {

        return !member.getName().equals(comment.getMember().getName());
    }
}
