package com.sparta.actualpractice.party;

import com.sparta.actualpractice.chat.ChatRoom;
import com.sparta.actualpractice.chat.ChatRoomRepository;
import com.sparta.actualpractice.member.Member;
import com.sparta.actualpractice.member.MemberRepository;
import com.sparta.actualpractice.member.MemberResponseDto;
import com.sparta.actualpractice.memberParty.MemberParty;
import com.sparta.actualpractice.memberParty.MemberPartyRepository;
import com.sparta.actualpractice.participant.ParticipantRepository;
import com.sparta.actualpractice.schedule.Schedule;
import com.sparta.actualpractice.schedule.ScheduleNullResponseDto;
import com.sparta.actualpractice.schedule.SchedulePartyResponseDto;
import com.sparta.actualpractice.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        Party party = new Party(partyRequestDto.getPartyName(), partyRequestDto.getPartyIntroduction(), createCode());
        Admin admin = new Admin(member, party);
        MemberParty memberParty = new MemberParty(member, party);
        ChatRoom chatRoom = new ChatRoom(party);
        
        partyRepository.save(party);
        adminRepository.save(admin);
        memberPartyRepository.save(memberParty);
        chatRoomRepository.save(chatRoom);

        party.updateChatRoom(chatRoom);
        party.updateAdmin(admin);

        partyRepository.save(party);

        return new ResponseEntity<>(new PartyResponseDto(party), HttpStatus.OK);
    }


    public ResponseEntity<?> getPartyList(Member member) {

        List<MemberParty> memberPartyList = memberPartyRepository.findAllByMember(member);

        List<PartyResponseDto> partyResponseDtoList = new ArrayList<>();
        List<Party> partyList = new ArrayList<>();

        for (MemberParty memberParty : memberPartyList) {
            Party party = partyRepository.findById(memberParty.getParty().getId()).orElseThrow(() -> new NullPointerException("?????? ????????? ????????????"));
            partyList.add(party);
        }

        for (Party party : partyList)
            partyResponseDtoList.add(new PartyResponseDto(party));

        return new ResponseEntity<>(partyResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getPartyInfo(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("?????? ????????? ???????????? ????????????."));

        if (!memberPartyRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("???????????? ?????? ????????? ?????? ????????? ????????????.");

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

            Member member1 = memberRepository.findById(memberParty.getMember().getId()).orElseThrow(() -> new IllegalArgumentException("????????? ???????????? ????????????."));
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

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("?????? ????????? ????????????"));

        if (validateAdmin(member, party))
            throw new IllegalArgumentException("?????? ????????? ????????? ??? ?????? ????????? ????????????.");

        party.updateInformation(partyRequestDto.getPartyName(), partyRequestDto.getPartyIntroduction());

        return new ResponseEntity<>("?????? ????????? ?????????????????????", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteParty(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("?????? ????????? ????????????"));

        if (validateAdmin(member, party))
            throw new IllegalArgumentException("?????? ????????? ????????? ??? ?????? ????????? ????????????.");

        partyRepository.delete(party);

        return new ResponseEntity<>("????????? ?????????????????????.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> withdrawParty(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("?????? ????????? ????????????"));

        if(adminRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("???????????? ?????? ???????????? ??? ??? ????????????.");

        memberPartyRepository.delete(memberPartyRepository.findByMemberAndParty(member, party).orElseThrow(() -> new NullPointerException("?????? ????????? ????????? ????????? ????????????.")));

        return new ResponseEntity<>("???????????? ????????? ???????????????.", HttpStatus.OK);

    }

    public ResponseEntity<?> getCode(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("?????? ????????? ???????????? ????????????."));

        memberPartyRepository.findByMemberAndParty(member, party).orElseThrow(() -> new NullPointerException("?????? ????????? ????????? ??? ?????? ????????? ????????????."));

        return new ResponseEntity<>(new PartyCodeResponseDto(party), HttpStatus.OK);
    }

    public ResponseEntity<?> joinGroup(PartyCodeRequestDto partyCodeRequestDto, Member member) {

        Party party = partyRepository.findByCode(partyCodeRequestDto.getCode()).orElseThrow(() -> new NullPointerException("?????? ????????? ???????????? ????????????."));

        if (memberPartyRepository.existsByMemberAndParty(member,party))
            throw new IllegalArgumentException("?????? ????????? ?????? ????????? ???????????????.");

        MemberParty memberParty = new MemberParty(member, party);

        memberPartyRepository.save(memberParty);

        return new ResponseEntity<>(new PartyResponseDto(party), HttpStatus.OK);
    }

    public boolean validateAdmin(Member member, Party party) {

        return !adminRepository.existsByMemberAndParty(member, party);
    }

    public String createCode() {

        String uuid = UUID.randomUUID().toString().substring(0,8);

        if (partyRepository.existsByCode(uuid))
            createCode();

        return uuid;
    }

    // ?????? ?????? ?????????
    // ?????? 0??? 0??? 0?????? ??????
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateCode() {

        List<Party> partyList = partyRepository.findAll();

        for (Party party : partyList) {

            if ( party.getCode() != null) {
                party.updateCode(createCode());
            }
        }
    }

}
