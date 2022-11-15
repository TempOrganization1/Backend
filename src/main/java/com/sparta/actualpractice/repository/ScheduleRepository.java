package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Party;
import com.sparta.actualpractice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByMember(Member member);
    List<Schedule> findAllByParty(Party party);

    List<Schedule> findAllByMemberOrderByTimeAsc(Member member);

    List<Schedule> findAllByPartyOrderByTimeAsc(Party party);

    boolean existsByIdAndMember(Long id, Member member);
}
