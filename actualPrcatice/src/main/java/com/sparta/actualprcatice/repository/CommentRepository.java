package com.sparta.actualprcatice.repository;

import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoardOrderByCreatedAtDesc(Board board);
}
