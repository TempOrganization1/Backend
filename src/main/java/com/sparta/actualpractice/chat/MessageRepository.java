package com.sparta.actualpractice.chat;

import com.sparta.actualpractice.chat.Message;
import com.sparta.actualpractice.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findTop500ByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    List<Message> findAllByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    Message findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
    Page<Message> findAllByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom, Pageable pageable);
}
