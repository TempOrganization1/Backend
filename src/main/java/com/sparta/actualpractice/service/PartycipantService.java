package com.sparta.actualpractice.service;

import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Participant;
import com.sparta.actualpractice.entity.Schedule;
import com.sparta.actualpractice.repository.MemberRepository;
import com.sparta.actualpractice.repository.ParticipantRepository;
import com.sparta.actualpractice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartycipantService {

    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ResponseEntity<?> createParticipant(Long scheduleId, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(()-> new NullPointerException("해당 유저가 없습니다."));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()-> new NullPointerException("해당 일정이 없습니다."));

        if(participantRepository.existsByScheduleAndMember(schedule, member)){
            participantRepository.deleteByScheduleAndMember(schedule, member);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        else{
            participantRepository.save(new Participant(schedule, member));
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
    }
}
