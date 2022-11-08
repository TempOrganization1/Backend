package com.sparta.actualprcatice.repository;

import com.example.consolelog.entity.Board;
import com.example.consolelog.entity.Heart;
import com.example.consolelog.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart ,Long> {

    boolean existsByBoardAndMember(Board board, Member member);

    Heart findByBoardAndMember(Board board, Member member);

}
