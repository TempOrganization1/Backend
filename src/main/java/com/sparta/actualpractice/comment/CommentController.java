package com.sparta.actualpractice.comment;

import com.sparta.actualpractice.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{album_id}/comments")
    public ResponseEntity<?> createComment(@PathVariable(name = "album_id") Long albumId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return commentService.createComment(albumId, commentRequestDto, memberDetails.getMember());
    }

    @PutMapping("/comments/{comment_id}")
    public ResponseEntity<?> updateComment(@PathVariable(name = "comment_id") Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return commentService.updateComment(commentId, commentRequestDto, memberDetails.getMember());
    }

    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "comment_id") Long commentId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        return commentService.deleteComment(commentId, memberDetails.getMember());
    }

}