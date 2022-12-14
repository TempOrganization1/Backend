package com.sparta.actualpractice.party;

import com.sparta.actualpractice.chat.ChatRoom;
import com.sparta.actualpractice.chat.ChatRoomRepository;
import com.sparta.actualpractice.exception.ErrorCode;
import com.sparta.actualpractice.exception.WefExceptions;
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

        Party party = new Party(partyRequestDto.getPartyName(),partyRequestDto.getPartyIntroduction(), createCode());
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
            Party party = partyRepository.findById(memberParty.getParty().getId()).orElseThrow(() -> new WefExceptions(ErrorCode.NOT_FOUND_PARTY));
            partyList.add(party);
        }

        for (Party party : partyList)
            partyResponseDtoList.add(new PartyResponseDto(party));

        return new ResponseEntity<>(partyResponseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> getPartyInfo(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new WefExceptions(ErrorCode.NOT_FOUND_PARTY));

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

        party.updateInformation(partyRequestDto.getPartyName(), partyRequestDto.getPartyIntroduction());

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

    @Transactional
    public ResponseEntity<?> withdrawParty(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 없습니다"));

        if(adminRepository.existsByMemberAndParty(member, party))
            throw new IllegalArgumentException("그룹장은 그룹 나가기를 할 수 없습니다.");

        memberPartyRepository.delete(memberPartyRepository.findByMemberAndParty(member, party).orElseThrow(() -> new NullPointerException("해당 유저는 그룹의 멤버가 아닙니다.")));

        return new ResponseEntity<>("그룹에서 나가기 되었습니다.", HttpStatus.OK);

    }

    public ResponseEntity<?> getCode(Long partyId, Member member) {

        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        memberPartyRepository.findByMemberAndParty(member, party).orElseThrow(() -> new NullPointerException("해당 그룹에 초대할 수 있는 권한이 없습니다."));

        return new ResponseEntity<>(new PartyCodeResponseDto(party), HttpStatus.OK);
    }

    public ResponseEntity<?> joinGroup(PartyCodeRequestDto partyCodeRequestDto, Member member) {

        Party party = partyRepository.findByCode(partyCodeRequestDto.getCode()).orElseThrow(() -> new NullPointerException("해당 그룹이 존재하지 않습니다."));

        if (memberPartyRepository.existsByMemberAndParty(member,party))
            throw new IllegalArgumentException("해당 그룹에 이미 가입된 유저입니다.");

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

    // 초대 코드 바꾸기
    // 매일 0시 0분 0초에 변경
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
