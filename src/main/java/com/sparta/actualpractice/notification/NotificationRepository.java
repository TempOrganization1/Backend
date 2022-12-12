package com.sparta.actualpractice.notification;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByMemberOrderByIdDesc(Member member);
    void deleteAllByMember(Member member);
    boolean existsByMemberAndId(Member member, Long id);

    Long countByIsReadFalseAndMember(Member member);
}
