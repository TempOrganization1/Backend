package com.sparta.actualpractice.repository;

import com.sparta.actualpractice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByMember_Id(Long memberId);
    List<Schedule> findAllByParty_Id(Long partyId);

    List<Schedule> findAllByMember_IdOrderByTimeAsc(Long memberId);

    List<Schedule> findAllByParty_IdOrderByTimeAsc(Long partyId);


}
