package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Message;
import com.sparta.actualpractice.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findTop100ByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
    Message findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
    Page<Message> findAllByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom, Pageable pageable);
}
