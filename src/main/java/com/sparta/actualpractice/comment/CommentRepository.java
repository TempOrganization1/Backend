package com.sparta.actualpractice.comment;

import com.sparta.actualpractice.album.Album;
import com.sparta.actualpractice.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAlbumOrderByCreatedAtDesc(Album album);
}