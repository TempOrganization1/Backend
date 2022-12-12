package com.sparta.actualpractice.participant;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.participant.Participant;
import com.sparta.actualpractice.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByScheduleAndMember(Schedule schedule, Member member);

    List<Participant> findAllBySchedule(Schedule schedule);

    void deleteByScheduleAndMember(Schedule schedule, Member member);
}
