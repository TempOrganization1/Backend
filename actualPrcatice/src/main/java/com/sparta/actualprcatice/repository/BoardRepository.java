package com.sparta.actualprcatice.repository;

import com.example.consolelog.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>{

    List<Board> findAllByOrderByCreatedAtDesc();

    Slice<Board> findAllBy(Pageable pageable);
    List<Board> findDistinctByCreatedAtBetween(LocalDateTime before, LocalDateTime after);
}
