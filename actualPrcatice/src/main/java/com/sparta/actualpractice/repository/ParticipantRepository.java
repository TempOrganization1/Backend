package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Participant;
import com.sparta.actualpractice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByScheduleAndMember(Schedule schedule, Member member);

    void deleteByScheduleAndMember(Schedule schedule, Member member);
}
