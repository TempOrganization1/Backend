package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAlbumOrderByCreatedAtDesc(Album album);
    Optional<Comment> findById(Long commentId);
}
