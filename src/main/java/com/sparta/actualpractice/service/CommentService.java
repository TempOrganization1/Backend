package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.CommentRequestDto;
import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Comment;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.repository.AlbumRepository;
import com.sparta.actualpractice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AlbumRepository albumRepository;

    private final CommentRepository commentRepository;

    public ResponseEntity<?> createComment(Long albumId, CommentRequestDto commentRequestDto, Member member) {

        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NullPointerException("해당 사진이 존재하지 않습니다."));

        Comment comment = new Comment(commentRequestDto, album, member);

        commentRepository.save(comment);

        return new ResponseEntity<>("댓글이 등록되었습니다.", HttpStatus.OK);
    }


}