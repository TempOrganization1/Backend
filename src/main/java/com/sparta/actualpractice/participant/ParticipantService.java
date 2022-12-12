package com.sparta.actualpractice.participant;

import com.sparta.actualpractice.notification.NotificationResponseDto;
import com.sparta.actualpractice.notification.AlarmType;
import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.notification.NotificationService;
import com.sparta.actualpractice.schedule.Schedule;
import com.sparta.actualpractice.member.MemberRepository;
import com.sparta.actualpractice.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final NotificationService notificationService;

    @Transactional
    public ResponseEntity<?> createParticipant(Long scheduleId, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(()-> new NullPointerException("해당 유저가 없습니다."));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()-> new NullPointerException("해당 일정이 없습니다."));

        if(participantRepository.existsByScheduleAndMember(schedule, member)) {
            participantRepository.deleteByScheduleAndMember(schedule, member);

            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        else {
            participantRepository.save(new Participant(schedule, member));

            if(!schedule.getMember().getId().equals(memberId))
                notificationService.send(schedule.getMember(), new NotificationResponseDto(schedule, member, AlarmType.schedule));

            return new ResponseEntity<>(true,HttpStatus.OK);
        }
    }
}
