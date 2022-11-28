package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.ScheduleRequestDto;
import com.sparta.actualpractice.dto.response.*;
import com.sparta.actualpractice.entity.*;
import com.sparta.actualpractice.repository.*;
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
    private final MemberRepository memberRepository;

    public ResponseEntity<?> createSchedules(Long partyId, Member member, ScheduleRequestDto scheduleRequestDto) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if(validateMemberAndParty(member, party))
            throw new IllegalArgumentException("사용자는 해당 그룹에 대한 접근 할 권한이 없습니다. ");

        Schedule schedule = new Schedule(member, scheduleRequestDto, party);

        scheduleRepository.save(schedule);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);
    }

    public ResponseEntity<?> getScheduleList(Member member) {

        List<MemberParty> memberPartyList = memberPartyRepository.findAllByMember(member);
        List<ScheduleListResponseDto> scheduleListResponseDtoList = new ArrayList<>();

        for (MemberParty memberParty : memberPartyList) {

            List<Schedule> scheduleList = scheduleRepository.findAllByParty(memberParty.getParty());

            for (Schedule schedule : scheduleList)
                scheduleListResponseDtoList.add(new ScheduleListResponseDto(schedule));
        }

        return new ResponseEntity<>(scheduleListResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getPopularSchedule(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if (validateMemberAndParty(member, party))
            throw new IllegalArgumentException("사용자는 해당 그룹에 대한 접근 할 권한이 없습니다. ");

        List<Schedule> scheduleList = scheduleRepository.findAllByParty(party);

        Long popularScheduleId = 0L;
        int num = -1;

        for (Schedule schedule : scheduleList) {

            if(schedule.getParticipantList().size() > num) {

                num = schedule.getParticipantList().size();
                popularScheduleId = schedule.getId();
            }
        }

        Schedule schedule = scheduleRepository.findById(popularScheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));

        Boolean isParticipant = participantRepository.existsByScheduleAndMember(schedule, member);

        List<MemberParty> memberPartyList = memberPartyRepository.findAllByParty(party);

        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

        for (MemberParty memberParty : memberPartyList){
            Member member1 = memberRepository.findById(memberParty.getId()).orElseThrow(() -> new IllegalArgumentException("맴버가 존재하지 않습니다."));
            memberResponseDtoList.add(new MemberResponseDto(member1));
        }

        return new ResponseEntity<>(new SchedulePartyResponseDto(schedule, isParticipant, party, memberResponseDtoList) , HttpStatus.OK);
    }

    public ResponseEntity<?> getPartyScheduleList(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if(validateMemberAndParty(member, party))
            throw new IllegalArgumentException("사용자는 해당 그룹에 대한 접근 할 권한이 없습니다. ");

        List<Schedule> scheduleList = scheduleRepository.findAllByPartyOrderByTimeAsc(party);

        List<ScheduleListResponseDto> scheduleListResponseDtoList = new ArrayList<>();

        for (Schedule schedule : scheduleList)
            scheduleListResponseDtoList.add(new ScheduleListResponseDto(schedule));

        return new ResponseEntity<>(scheduleListResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getSchedule(Long scheduleId, Member member) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));
        Boolean isParticipant = participantRepository.existsByScheduleAndMember(schedule, member);

        List<Participant> participantList = participantRepository.findAllBySchedule(schedule);
        List<ParticipantReponseDto> participantResponseDtoList = new ArrayList<>();

        for (Participant participant : participantList) {

            if (scheduleId.equals(participant.getSchedule().getId()))
                participantResponseDtoList.add(new ParticipantReponseDto(participant));
        }

        return new ResponseEntity<>(new ScheduleResponseDto(schedule, participantResponseDtoList, isParticipant), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateSchedule(Long scheduleId, ScheduleRequestDto scheduleRequestDto, Member member) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));

        if(validateMemberAndSchedule(member, schedule))
            throw new IllegalArgumentException("해당 일정의 작성자가 아닙니다.");

        schedule.updateInformation(scheduleRequestDto);

        return new ResponseEntity<>("해당 일정이 수정되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteSchedule(Long scheduleId, Member member) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));

        if(validateMemberAndSchedule(member, schedule))
            throw new IllegalArgumentException("해당 일정의 작성자가 아닙니다.");

        scheduleRepository.delete(schedule);

        return new ResponseEntity<>("해당 일정이 삭제되었습니다.", HttpStatus.OK);
    }

    public boolean validateMemberAndSchedule(Member member, Schedule schedule){

        return !scheduleRepository.existsByIdAndMember(schedule.getId(), member);
    }

    public boolean validateMemberAndParty(Member member, Party party) {

        return !memberPartyRepository.existsByMemberAndParty(member, party);
    }
}
