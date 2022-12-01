package com.sparta.actualpractice.service;

import com.sparta.actualpractice.dto.request.PartyRequestDto;
import com.sparta.actualpractice.dto.response.MemberResponseDto;
import com.sparta.actualpractice.dto.response.PartyResponseDto;
import com.sparta.actualpractice.dto.response.SchedulePartyResponseDto;
import com.sparta.actualpractice.dto.response.ScheduleNullResponseDto;
import com.sparta.actualpractice.entity.*;
import com.sparta.actualpractice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final AdminRepository adminRepository;
    private final MemberPartyRepository memberPartyRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;

    public ResponseEntity<?> createParty(PartyRequestDto partyRequestDto, Member member) {

        Party party = new Party(partyRequestDto);
        Admin admin = new Admin(member, party);
        MemberParty memberParty = new MemberParty(member, party);
        ChatRoom chatRoom = new ChatRoom(party, party.getName() + "의 채팅방");

        partyRepository.save(party);
        adminRepository.save(admin);
        memberPartyRepository.save(memberParty);
        chatRoomRepository.save(chatRoom);

        return new ResponseEntity<>(new PartyResponseDto(party), HttpStatus.OK);
    }


    public ResponseEntity<?> getPartyList(Member member) {

        List<MemberParty> memberPartyList = memberPartyRepository.findAllByMember(member);

        List<PartyResponseDto> partyResponseDtoList = new ArrayList<>();
        List<Party> partyList = new ArrayList<>();

        for (MemberParty memberParty : memberPartyList) {
            Party party = partyRepository.findById(memberParty.getParty().getId()).orElseThrow(() -> new NullPointerException("해당 그룹이 없습니다"));
            partyList.add(party);
        }

        for (Party party : partyList)
            partyResponseDtoList.add(new PartyResponseDto(party));

        return new ResponseEntity<>(partyResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getPartyInfo(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if (!memberPartyRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("사용자는 해당 그룹에 대한 회원이 아닙니다.");

        List<Schedule> scheduleList = scheduleRepository.findAllByParty(party);

        Long popularScheduleId = 0L;
        int num = -1;

        for (Schedule schedule : scheduleList) {

            if (schedule.getParticipantList().size() > num) {

                num = schedule.getParticipantList().size();
                popularScheduleId = schedule.getId();
            }
        }

        List<MemberParty> memberPartyList = memberPartyRepository.findAllByParty(party);

        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

        for (MemberParty memberParty : memberPartyList) {

            Member member1 = memberRepository.findById(memberParty.getMember().getId()).orElseThrow(() -> new IllegalArgumentException("맴버가 존재하지 않습니다."));
            memberResponseDtoList.add(new MemberResponseDto(member1));
        }

        Schedule schedule = scheduleRepository.findById(popularScheduleId).orElse(null);

        if (schedule == null)
            return new ResponseEntity<>(new ScheduleNullResponseDto(party, memberResponseDtoList), HttpStatus.OK);

        Boolean isParticipant = participantRepository.existsByScheduleAndMember(schedule, member);

        return new ResponseEntity<>(new SchedulePartyResponseDto(schedule, isParticipant, party, memberResponseDtoList) , HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateParty(Long partyId, PartyRequestDto partyRequestDto, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 없습니다"));

        if (validateAdmin(member, party))
            throw new IllegalArgumentException("그룹 정보를 수정할 수 있는 권한이 없습니다.");

        party.updateInformation(partyRequestDto);

        return new ResponseEntity<>("그룹 정보가 수정되었습니다", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteParty(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 없습니다"));

        if (validateAdmin(member, party))
            throw new IllegalArgumentException("그룹 정보를 삭제할 수 있는 권한이 없습니다.");

        partyRepository.delete(party);

        return new ResponseEntity<>("그룹이 삭제되었습니다.", HttpStatus.OK);
    }

    public boolean validateAdmin(Member member, Party party) {

        return !adminRepository.existsByMemberAndParty(member, party);
    }

}
