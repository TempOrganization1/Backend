package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.ScheduleRequestDto;
import com.sparta.actualpractice.dto.response.ParticipantReponseDto;
import com.sparta.actualpractice.dto.response.ScheduleListResponseDto;
import com.sparta.actualpractice.dto.response.ScheduleResponseDto;
import com.sparta.actualpractice.entity.*;
import com.sparta.actualpractice.repository.MemberPartyRepository;
import com.sparta.actualpractice.repository.ParticipantRepository;
import com.sparta.actualpractice.repository.PartyRepository;
import com.sparta.actualpractice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PartyRepository partyRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final ParticipantRepository participantRepository;

    public ResponseEntity<?> createSchedules(Long partyId, Member member, ScheduleRequestDto scheduleRequestDto) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        validateMember(member, party);

        Schedule schedule = new Schedule(member, scheduleRequestDto, party);

        scheduleRepository.save(schedule);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);
    }

    public ResponseEntity<?> getScheduleList(Member member) {

        List<MemberParty> memberPartyList = memberPartyRepository.findAllByMember_Id(member.getId());
        List<ScheduleListResponseDto> scheduleListResponseDtoList = new ArrayList<>();

        for (MemberParty memberParty : memberPartyList) {

            List<Schedule> scheduleList = scheduleRepository.findAllByParty(memberParty.getParty());

            for (Schedule schedule : scheduleList) {

                scheduleListResponseDtoList.add(new ScheduleListResponseDto(schedule, schedule.getParty().getName()));
            }
        }

        return new ResponseEntity<>(scheduleListResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getPartyScheduleList(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        validateMember(member, party);

        List<Schedule> scheduleList = scheduleRepository.findAllByPartyOrderByTimeAsc(party);
        List<ScheduleListResponseDto> scheduleListResponseDtoList = new ArrayList<>();

        for (Schedule schedule : scheduleList)
            scheduleListResponseDtoList.add(new ScheduleListResponseDto(schedule));

        return new ResponseEntity<>(scheduleListResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getSchedule(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));

        List<Participant> participantList = participantRepository.findAllBySchedule(schedule);
        List<ParticipantReponseDto> participantResponseDtoList = new ArrayList<>();

        for (Participant participant : participantList) {

            if (scheduleId.equals(participant.getSchedule().getId()))
                participantResponseDtoList.add(new ParticipantReponseDto(participant));
        }

        return new ResponseEntity<>(new ScheduleResponseDto(schedule, participantResponseDtoList), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateSchedule(Long scheduleId, ScheduleRequestDto scheduleRequestDto, Member member) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));

        if(validateMember(member, schedule)){
            throw new IllegalArgumentException("해당 일정의 작성자가 아닙니다.");
        }

        schedule.update(scheduleRequestDto);

        return new ResponseEntity<>("해당 일정이 수정되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteSchedule(Long scheduleId, Member member) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));

        if(validateMember(member, schedule)){
            throw new IllegalArgumentException("해당 일정의 작성자가 아닙니다.");
        }

        scheduleRepository.delete(schedule);

        return new ResponseEntity<>("해당 일정이 삭제되었습니다.", HttpStatus.OK);
    }

    public boolean validateMember(Member member, Schedule schedule){

        return !schedule.getMember().getEmail().equals(member.getEmail());
    }

    public void validateMember(Member member, Party party) {

        if(!memberPartyRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("현재 사용자는 그룹에 접근 할 권한이 없습니다.");

    }
}