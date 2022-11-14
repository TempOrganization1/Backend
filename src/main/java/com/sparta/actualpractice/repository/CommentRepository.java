package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Album;
import com.sparta.actualpractice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAlbumOrderByCreatedAtDesc(Album album);
}