package com.sparta.actualprcatice.repository;

import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findTop1ByBoardOrderByCreatedAtAsc(Board board);
}
