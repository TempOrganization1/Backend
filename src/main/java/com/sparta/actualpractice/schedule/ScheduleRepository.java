package com.sparta.actualpractice.schedule;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.party.Party;
import com.sparta.actualpractice.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByParty(Party party);

    List<Schedule> findAllByPartyOrderByTimeAsc(Party party);

    boolean existsByIdAndMember(Long id, Member member);

}
