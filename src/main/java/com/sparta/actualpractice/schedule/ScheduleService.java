package com.sparta.actualpractice.schedule;

import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.memberParty.MemberParty;
import com.sparta.actualpractice.memberParty.MemberPartyRepository;
import com.sparta.actualpractice.participant.Participant;
import com.sparta.actualpractice.participant.ParticipantReponseDto;
import com.sparta.actualpractice.participant.ParticipantRepository;
import com.sparta.actualpractice.party.Party;
import com.sparta.actualpractice.party.PartyRepository;
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

        if (validateAndParty(member, party))
            throw new IllegalArgumentException("사용자는 해당 그룹에 대한 접근 할 권한이 없습니다. ");

        Schedule schedule = Schedule.builder()
                .title(scheduleRequestDto.getTitle())
                .content(scheduleRequestDto.getContent())
                .time(scheduleRequestDto.getDate()+ " " + scheduleRequestDto.getMeetTime())
                .place(scheduleRequestDto.getPlace().getAddress() + "," + scheduleRequestDto.getPlace().getPlaceName())
                .member(member)
                .party(party)
                .build();

        scheduleRepository.save(schedule);

        return new ResponseEntity<>(new ScheduleResponseDto(schedule, party), HttpStatus.OK);
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

    public ResponseEntity<?> getPartyScheduleList(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if (validateAndParty(member, party))
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

        if (validateSchedule(member, schedule))
            throw new IllegalArgumentException("해당 일정의 작성자가 아닙니다.");

        schedule.updateTitle(scheduleRequestDto.getTitle());
        schedule.updateContent(scheduleRequestDto.getContent());
        schedule.updateTime(scheduleRequestDto.getDate(), scheduleRequestDto.getMeetTime());
        schedule.updatePlace(scheduleRequestDto.getPlace().getAddress(), scheduleRequestDto.getPlace().getPlaceName());

        return new ResponseEntity<>("해당 일정이 수정되었습니다.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteSchedule(Long scheduleId, Member member) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new NullPointerException("해당 일정이 존재하지 않습니다."));

        if (validateSchedule(member, schedule))
            throw new IllegalArgumentException("해당 일정의 작성자가 아닙니다.");

        scheduleRepository.delete(schedule);

        return new ResponseEntity<>("해당 일정이 삭제되었습니다.", HttpStatus.OK);
    }

    public boolean validateSchedule(Member member, Schedule schedule) {

        return !scheduleRepository.existsByIdAndMember(schedule.getId(), member);
    }

    public boolean validateAndParty(Member member, Party party) {

        return !memberPartyRepository.existsByMemberAndParty(member, party);
    }
}
