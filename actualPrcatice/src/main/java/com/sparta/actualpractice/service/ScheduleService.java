package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.ScheduleRequestDto;
import com.sparta.actualpractice.dto.response.ParticipantReponseDto;
import com.sparta.actualpractice.dto.response.ScheduleResponseDto;
import com.sparta.actualpractice.entity.Member;
import com.sparta.actualpractice.entity.Participant;
import com.sparta.actualpractice.entity.Party;
import com.sparta.actualpractice.entity.Schedule;
import com.sparta.actualpractice.repository.ParticipantRepository;
import com.sparta.actualpractice.repository.PartyRepository;
import com.sparta.actualpractice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PartyRepository partyRepository;
    private final ParticipantRepository participantRepository;

    public ResponseEntity<?> createSchedules(Long partyId, Member member, ScheduleRequestDto scheduleRequestDto) {

        Party party = partyRepository.findById(partyId).orElseThrow(()-> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if (!validateMember(member,party)){
           throw new NullPointerException("해당 그룹에 포함되어 있지 않습니다.");
        }

        String gatherTime = scheduleRequestDto.getDate()+ " " +scheduleRequestDto.getMeetTime();
        String gatherPlace = scheduleRequestDto.getPlace().getAddress()+ "," +scheduleRequestDto.getPlace().getPlaceName();
        Schedule schedule = new Schedule(member, scheduleRequestDto, gatherTime, party, gatherPlace);

        scheduleRepository.save(schedule);

        return new ResponseEntity<>("일정이 추가되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> getSchedulesList(Member member) {

        List<Long> partyIdsList = new ArrayList<>();
        List<ScheduleResponseDto> scheduleResponseDtoList = new ArrayList<>();

        for(int i = 0; i < member.getMemberPartyList().size(); i++){

            Long partyId = member.getMemberPartyList().get(i).getParty().getId();
            partyIdsList.add(partyId);
        }

        List<Long> partyIdList = partyIdsList.stream().distinct().collect(Collectors.toList());

        for(Long partyId : partyIdList){

            List<Schedule> scheduleList = scheduleRepository.findAllByParty_IdOrderByTimeAsc(partyId);
            for(Schedule schedule : scheduleList){

                scheduleResponseDtoList.add(new ScheduleResponseDto(schedule.getParty().getName(), schedule.getId(), schedule.getMember().getName()));
            }
        }

        return new ResponseEntity<>(scheduleResponseDtoList,HttpStatus.OK);
    }

    public ResponseEntity<?> getPartySchedulesList(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(()-> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if (!validateMember(member,party)){
            throw new NullPointerException("해당 그룹에 포함되어 있지 않습니다.");
        }

        List<Schedule> scheduleList = scheduleRepository.findAllByParty_IdOrderByTimeAsc(partyId);
        List<ScheduleResponseDto> scheduleResponseDtoList = new ArrayList<>();

        for (Schedule schedule : scheduleList){

            scheduleResponseDtoList.add(new ScheduleResponseDto(schedule.getParty().getName(), schedule.getId(), schedule.getMember().getName()));
        }

        return new ResponseEntity<>(scheduleResponseDtoList,HttpStatus.OK);
    }

    public ResponseEntity<?> getSchedule(Long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()-> new NullPointerException("해당 일정이 존재하지 않습니다."));

        List<Participant> participantList = participantRepository.findAll();
        List<ParticipantReponseDto> participantResponseDtoList = new ArrayList<>();

        for(Participant participant : participantList){

            if(scheduleId.equals(participant.getSchedule().getId())) {
                participantResponseDtoList.add(new ParticipantReponseDto(participant));
            }
        }

        return new ResponseEntity<>(new ScheduleResponseDto(schedule,participantResponseDtoList),HttpStatus.OK);
    }

    public boolean validateMember(Member member, Party party) {

        boolean result = false;

        for (int i = 0; i < party.getMemberPartyList().size(); i++ ){
            if (member.getEmail().equals(party.getMemberPartyList().get(i).getMember().getEmail())) {
                result = true;
            }
        }

        return result;
    }
}
