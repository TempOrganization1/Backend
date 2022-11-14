package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Participant;
import com.sparta.actualpractice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByScheduleAndMember(Schedule schedule, Member member);

    List<Participant> findAllBySchedule(Schedule schedule);

    void deleteByScheduleAndMember(Schedule schedule, Member member);
}
