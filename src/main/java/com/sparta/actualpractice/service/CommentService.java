package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.CommentRequestDto;
import com.sparta.actualpractice.dto.response.CommentResponseDto;
import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Comment;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.repository.AlbumRepository;
import com.sparta.actualpractice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AlbumRepository albumRepository;

    private final CommentRepository commentRepository;

    public ResponseEntity<?> createComment(Long albumId, CommentRequestDto commentRequestDto, Member member) {

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NullPointerException("해당 사진이 존재하지 않습니다."));

        Comment comment = new Comment(commentRequestDto, album, member);

        commentRepository.save(comment);

        return new ResponseEntity<>(new CommentResponseDto(comment), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, Member member) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        if (validateMember(member, comment))
            throw new IllegalArgumentException("댓글 작성자와 현재 사용자가 일치하지 않습니다.");

        comment.updateContent(commentRequestDto);

        return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> deleteComment(Long commentId, Member member) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("해당 댓글이 존재하지 않습니다."));

        if (validateMember(member, comment))
            throw new IllegalArgumentException("댓글 작성자와 현재 사용자가 일치하지 않습니다.");

        commentRepository.delete(comment);

        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }

    public boolean validateMember(Member member, Comment comment) {
        return !member.getEmail().equals(comment.getMember().getEmail());
    }
}