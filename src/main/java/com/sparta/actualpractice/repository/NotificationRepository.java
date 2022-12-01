package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByMemberOrderByIdDesc(Member member);
    void deleteAllByMember(Member member);
    boolean existsByMemberAndId(Member member, Long id);

    Long countByIsReadFalseAndMember(Member member);
}
